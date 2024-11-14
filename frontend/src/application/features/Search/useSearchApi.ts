import { useQuery } from '@tanstack/react-query';
import { LatLngCoords } from './utils';

export type Pagination = {
  totalNumberOfPages: number;
  page: number;
};

export type City = {
  id: number;
  country: string;
  name: string;
  altNames: string[];
  admins: string[];
};

export type Filter = {
  id: number;
  name: string;
};

export type FrontCity = {
 score: number, city: City
}

export type Filters = {
  countries: Filter[];
  admins: Filter[];
};

const initialData: { pagination: Pagination; cities: FrontCity[]; filters: Filters } = {
  pagination: {
    totalNumberOfPages: 0,
    page: 0,
  },
  cities: [],
  filters: {
    countries: [],
    admins: [],
  },
};

export default function useSearch(
  search: string,
  currentPage: number,
  pageSize: number,
  position: LatLngCoords | null,
  radius: number | null,
  currentCountry: string | null,
  currentAdmin: string | null,
) {
  return useQuery({
    queryKey: ['query', search, currentPage, pageSize, position, radius, currentCountry, currentAdmin],
    initialData,
    queryFn: async () => {
      if (search.length === 0) {
        return initialData;
      }

      const query = search.toLowerCase();
      const suggestionsUrl = new URL(`${process.env.REACT_APP_API_URL}/suggestions`);

      let positionObj = {};
      if (position && radius) {
        positionObj = {
          latitude: position.latitude,
          longitude: position.longitude,
          radius,
        };
      }

      const { pagination, cities, filters } = await fetch(suggestionsUrl.href, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          query,
          filters: {
            geolocation:
              Object.keys(positionObj).length === 0
                ? undefined
                : {
                    ...positionObj,
                  },
            countries: currentCountry != null ? [currentCountry] : undefined,
            admins: currentAdmin != null ? [currentAdmin] : undefined,
          },
          pageInfos: {
            page: currentPage - 1,
            pageSize: pageSize,
          },
        }),
      }).then((response) => response.json());

      return {
        pagination: {
          page: pagination.page + 1,
          totalNumberOfPages: pagination.totalNumberOfPages,
        },
        cities,
        filters,
      };
    },
  });
}
