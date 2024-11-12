import React from 'react';
import SearchBar from './components/SearchBar/SearchBar';
import {City, Results} from './components/Results/Results';
import useObjectState from '../core/hooks/useObjectState';

import useDebounce from '../core/hooks/useDebounce';
import {askLocation} from '../core/utils/location';
import {useSearchParams} from "react-router-dom";
import useSearch from "./useSearchApi";
import {LatLngCoords} from "./utils";
import {isNaN} from "lodash";

enum URL_PARAMS {
    QUERY = "query",
    LATITUDE = "latitude",
    LONGITUDE = "longitude",
    SHOW = "show",
    FILTER = "filter",
    PAGE = "page",
}

type SearchState = {
    search: string;
    position: LatLngCoords | null;
    filterResult: boolean;
    showResults: boolean;
    page: number;
};
const retrievePositionFromSearchParams = (latitude: string | null, longitude: string | null) => {
    try {
        if (latitude && longitude) {
            const lat = parseFloat(latitude);
            const long = parseFloat(longitude);
            if(isNaN(latitude) || isNaN(long)) {
                return null
            }
            return {latitude: lat, longitude: long};
        }
    } catch {
        console.error('Failed to parse location: latitude or longitude is not a number');
    }
    return null;
}

const retrievePageFromSearchParams = (page: string | null) => {
    if (page) {
        const parsedNumber = parseInt(page)
        return isNaN(parsedNumber) ? 1 : parsedNumber;
    }
    return 1;
}

const Search: React.FunctionComponent = () => {
    const [searchParams, setSearchParams] = useSearchParams();

    const [state, setCurrentState] = useObjectState<SearchState>({
        search: searchParams.get(URL_PARAMS.QUERY) ?? "",
        position: retrievePositionFromSearchParams(searchParams.get(URL_PARAMS.LATITUDE), searchParams.get(URL_PARAMS.LONGITUDE)),
        filterResult: searchParams.get(URL_PARAMS.FILTER) === "true",
        showResults: searchParams.get(URL_PARAMS.SHOW) === "true",
        page: retrievePageFromSearchParams(searchParams.get(URL_PARAMS.PAGE)),
    });

    const debouncedSearch = useDebounce(state.search);

    const {data, isError, isLoading} = useSearch(state.page, state.position, debouncedSearch);

    const isResultDisplayable = state.showResults && debouncedSearch === state.search;

    const onChange = (name: string, value: string) => {
        if (searchParams.has(name)) {
            searchParams.set(name, value);
        } else {
            searchParams.append(name, value);
        }
        setSearchParams(searchParams);
    }

    if (isError) {
        return (
            <p className={'text-red-500'} data-testid={'error'}>
                Error :(
            </p>
        );
    }

    return (
        <div data-testid='search' className='text-center'>
            {
                <div className='bg-charcoal flex flex-col items-center justify-center min-h-[100vh]'>
                    <SearchBar
                        placeholder='Search cities'
                        currentSearch={state.search}
                        onSearchTextChange={(search) => {
                            setCurrentState({search, showResults: false});
                            onChange(URL_PARAMS.QUERY, search);
                        }}
                        onSearchButtonClicked={() => {
                            setCurrentState({filterResult: false, showResults: true});
                            onChange(URL_PARAMS.SHOW, `${true}`)
                            onChange(URL_PARAMS.FILTER, `${true}`)
                        }}
                        onSuggestionsClicked={(search) => {
                            setCurrentState({search, filterResult: true, showResults: true});
                            onChange(URL_PARAMS.QUERY, search);
                        }}
                        cities={state.showResults ? [] : data.cities.map((c: City) => c.name)}
                        onLocationAsked={() =>
                            askLocation((position) => {
                                    setCurrentState({
                                        position: position,
                                        showResults: false,
                                    });
                                    onChange(URL_PARAMS.LATITUDE, position.latitude.toString());
                                    onChange(URL_PARAMS.LONGITUDE, position.longitude.toString());
                                },
                            )
                        }
                        isLoading={isLoading}
                    />
                    {isResultDisplayable && (
                        <Results
                            cities={data.cities}
                            currentPage={state.page}
                            totalNumberOfPages={data.totalNumberOfPages}
                            onPrevious={() => {
                                setCurrentState({page: state.page - 1 <= 0 ? 1 : state.page - 1});
                            }}
                            onNext={() => {
                                setCurrentState({
                                    page: state.page + 1 >= data.totalNumberOfPages ? data.totalNumberOfPages : state.page + 1,
                                });
                            }}
                        />
                    )}
                </div>
            }
        </div>
    );
};

export default Search;
