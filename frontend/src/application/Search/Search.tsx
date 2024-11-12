import React from 'react';
import SearchBar from './components/SearchBar/SearchBar';
import { City, Results } from './components/Results/Results';
import useObjectState from '../core/hooks/useObjectState';

import useDebounce from '../core/hooks/useDebounce';
import { askLocation } from '../core/utils/location';
import { useSearchParams } from 'react-router-dom';
import useSearch from './useSearchApi';
import { LatLngCoords } from './utils';
import { isNaN } from 'lodash';

enum URL_PARAMS {
  QUERY = 'query',
  LATITUDE = 'latitude',
  LONGITUDE = 'longitude',
  SHOW = 'show',
  FILTER = 'filter',
  PAGE = 'page',
  PAGE_SIZE = 'page_size',
  RADIUS = 'radius',
  COUNTRY = 'country',
  ADMIN = 'admin',
}

type SearchState = {
  search: string;
  position: LatLngCoords | null;
  radius: number;
  filterResult: boolean;
  showResults: boolean;
  page: number;
  pageSize: number;
  selectedCountry: string | null;
  currentAdmin: string | null;

};
const retrievePositionFromSearchParams = (latitude: string | null, longitude: string | null) => {
  try {
    if (latitude && longitude) {
      const lat = parseFloat(latitude);
      const long = parseFloat(longitude);
      if (isNaN(latitude) || isNaN(long)) {
        return null;
      }
      return { latitude: lat, longitude: long };
    }
  } catch {
    console.error('Failed to parse location: latitude or longitude is not a number');
  }
  return null;
};

const retrievePageFromSearchParams = (page: string | null, defaultValue?: number | null) => {
  if (page) {
    const parsedNumber = parseInt(page);
    return isNaN(parsedNumber) ? (defaultValue ?? 1) : parsedNumber;
  }
  return defaultValue ?? 1;
};

const Search: React.FunctionComponent = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const [state, setCurrentState] = useObjectState<SearchState>({
    search: searchParams.get(URL_PARAMS.QUERY) ?? '',
    filterResult: searchParams.get(URL_PARAMS.FILTER) === 'true',
    showResults: searchParams.get(URL_PARAMS.SHOW) === 'true',
    page: retrievePageFromSearchParams(searchParams.get(URL_PARAMS.PAGE), 1),
    pageSize: retrievePageFromSearchParams(searchParams.get(URL_PARAMS.PAGE_SIZE), 5),
    position: retrievePositionFromSearchParams(
      searchParams.get(URL_PARAMS.LATITUDE),
      searchParams.get(URL_PARAMS.LONGITUDE),
    ),
    radius: retrievePageFromSearchParams(searchParams.get(URL_PARAMS.RADIUS), 100),
    selectedCountry: searchParams.get(URL_PARAMS.COUNTRY),
    currentAdmin: searchParams.get(URL_PARAMS.ADMIN),
  });

  const debouncedSearch = useDebounce(state.search);

  const { data, isError, isLoading } = useSearch(
    debouncedSearch,
    state.page,
    state.pageSize,
    state.position,
    state.radius,
    state.selectedCountry,
      state.currentAdmin
  );

  const isResultDisplayable = state.showResults && debouncedSearch === state.search;

  const onChange = (name: string, value: string | null) => {
    if (searchParams.has(name) && value !== null) {
      searchParams.set(name, value);
    } else if (value !== null) {
      searchParams.append(name, value);
    }
    setSearchParams(searchParams);
  };

  if (isError) {
    return (
      <p className={'text-red-500'} data-testid={'error'}>
        Error :(
      </p>
    );
  }

  return (
    <div data-testid='search' className='text-center bg-charcoal p-10'>
      {
        <div className='bg-charcoal flex flex-col items-center justify-center min-h-[100vh]'>
          <SearchBar
            placeholder='Search cities'
            currentSearch={state.search}
            onRadiusChange={(radius: number) => {
              setCurrentState({ radius });
              onChange(URL_PARAMS.RADIUS, radius.toString());
            }}
            onPageSizeChange={(pageSize: number) => {
              setCurrentState({ pageSize });
              onChange(URL_PARAMS.PAGE_SIZE, pageSize.toString());
            }}
            onSearchTextChange={(search) => {
              setCurrentState({ search, showResults: false });
              onChange(URL_PARAMS.QUERY, search);
            }}
            onSearchButtonClicked={() => {
              setCurrentState({ filterResult: false, showResults: true });
              onChange(URL_PARAMS.SHOW, `${true}`);
              onChange(URL_PARAMS.FILTER, `${true}`);
            }}
            countries={data.countries}
            currentCountry={state.selectedCountry}
            onCountryChange={(country) => {
              setCurrentState({ selectedCountry: country });
              onChange(URL_PARAMS.COUNTRY, country);
            }}
            onSuggestionsClicked={(search) => {
              setCurrentState({ search, filterResult: true, showResults: true });
              onChange(URL_PARAMS.QUERY, search);
            }}
            cities={state.showResults && data.cities !== undefined ? [] : data.cities?.map((c: City) => c.name)}
            onLocationAsked={() =>
              askLocation((position) => {
                setCurrentState({
                  position: position,
                  showResults: false,
                });
                onChange(URL_PARAMS.LATITUDE, position.latitude.toString());
                onChange(URL_PARAMS.LONGITUDE, position.longitude.toString());
              })
            }
            isLoading={isLoading}
            currentPage={state.page}
            currentRadius={state.radius}
          />
          {isResultDisplayable && (
            <Results
              cities={data.cities}
              currentPage={state.page}
              totalNumberOfPages={data.totalNumberOfPages}
              onPrevious={() => {
                setCurrentState({ page: state.page - 1 <= 0 ? 1 : state.page - 1 });
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
