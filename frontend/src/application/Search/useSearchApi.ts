import {City} from './components/Results/Results';
import {useQuery} from '@tanstack/react-query';
import {LatLngCoords} from './utils';

export type Filter = {
    id: number;
    name: string;
};

const initialData = {
    cities: [] as City[],
    countries: [] as Filter[],
    admins: [] as Filter[],
    totalNumberOfPages: 0,
    page: 0,
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

            const {cities, countries, totalNumberOfPages, page, admins} = await fetch(suggestionsUrl.href, {
                method: 'POST',
                headers: {
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    query,
                    page: currentPage - 1,
                    pageSize: pageSize,
                    selectedCountry: currentCountry,
                    selectedAdmin: currentAdmin,
                    ...positionObj,
                }),
            }).then((response) => response.json());

            return {cities, totalNumberOfPages, page: page + 1, countries, admins};
        },
    });
}
