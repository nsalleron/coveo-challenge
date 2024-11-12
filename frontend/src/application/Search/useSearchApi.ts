import {City} from "./components/Results/Results";
import {useQuery} from "@tanstack/react-query";
import {LatLngCoords} from "./utils";

const initialData = {
    cities: [] as City[],
    totalNumberOfPages: 0,
    page: 0,
};

function appendUserLocation(searchUrl: URL, position: LatLngCoords) {
    searchUrl.searchParams.append('latitude', `${position.latitude}`)
    searchUrl.searchParams.append('longitude', `${position.longitude}`)
}

export default function useSearch(currentPage: number, position: LatLngCoords | null, search: string) {
    return useQuery({
        queryKey: ['query', search, currentPage],
        initialData,
        queryFn: async () => {
            if (search.length === 0) {
                return initialData;
            }

            const q = search.toLowerCase();
            const searchUrl = new URL(`${process.env.REACT_APP_API_URL}/suggestions`);
            searchUrl.searchParams.append('q', q);
            searchUrl.searchParams.append('page', `${currentPage - 1}`);
            if (position) {
                appendUserLocation(searchUrl, position);
            }
            console.log(searchUrl.href)
            const {cities, totalNumberOfPages, page} = await fetch(searchUrl.href).then((response) => response.json());

            return {cities, totalNumberOfPages, page: page + 1};
        },
    });
}
