import React, { useEffect } from 'react';
import SearchBar from './components/SearchBar/SearchBar';
import { Results } from './components/Results/Results';
import useObjectState from '../../core/hooks/useObjectState';

import useDebounce from '../../core/hooks/useDebounce';
import { askLocation } from '../../core/utils/location';
import { useSearchParams } from 'react-router-dom';
import useSearch, {FrontCity} from './useSearchApi';
import {
  LatLngCoords,
  retrievePageFromSearchParams,
  retrievePositionFromSearchParams,
  updateSearchParams,
  URL_SEARCH_PARAMS,
} from './utils';
import { isEmpty } from 'lodash';

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

const Search: React.FunctionComponent = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const [state, setCurrentState] = useObjectState<SearchState>({
    search: searchParams.get(URL_SEARCH_PARAMS.QUERY) ?? '',
    filterResult: searchParams.get(URL_SEARCH_PARAMS.FILTER) === 'true',
    showResults: searchParams.get(URL_SEARCH_PARAMS.SHOW) === 'true',
    page: retrievePageFromSearchParams(searchParams.get(URL_SEARCH_PARAMS.PAGE), 1),
    pageSize: retrievePageFromSearchParams(searchParams.get(URL_SEARCH_PARAMS.PAGE_SIZE), 5),
    position: retrievePositionFromSearchParams(
      searchParams.get(URL_SEARCH_PARAMS.LATITUDE),
      searchParams.get(URL_SEARCH_PARAMS.LONGITUDE),
    ),
    radius: retrievePageFromSearchParams(searchParams.get(URL_SEARCH_PARAMS.RADIUS), 100),
    selectedCountry: searchParams.get(URL_SEARCH_PARAMS.COUNTRY),
    currentAdmin: searchParams.get(URL_SEARCH_PARAMS.ADMIN),
  });

  useEffect(() => {
    updateSearchParams(searchParams, URL_SEARCH_PARAMS.QUERY, isEmpty(state.search) ? null : state.search);
    updateSearchParams(
      searchParams,
      URL_SEARCH_PARAMS.RADIUS,
      isEmpty(state.position) ? null : (state.radius.toString() ?? '100'),
    );
    updateSearchParams(searchParams, URL_SEARCH_PARAMS.PAGE, state.page.toString());
    updateSearchParams(searchParams, URL_SEARCH_PARAMS.PAGE_SIZE, state.pageSize.toString());
    updateSearchParams(searchParams, URL_SEARCH_PARAMS.COUNTRY, state.selectedCountry);
    updateSearchParams(searchParams, URL_SEARCH_PARAMS.SHOW, `${state.showResults}`);
    updateSearchParams(searchParams, URL_SEARCH_PARAMS.FILTER, `${state.filterResult}`);
    if (state.position) {
      updateSearchParams(searchParams, URL_SEARCH_PARAMS.LATITUDE, state.position.latitude.toString());
      updateSearchParams(searchParams, URL_SEARCH_PARAMS.LONGITUDE, state.position.longitude.toString());
    }
    setSearchParams(searchParams);
  }, [
    state.search,
    state.radius,
    state.page,
    state.pageSize,
    state.selectedCountry,
    state.showResults,
    state.filterResult,
    state.position,
    searchParams,
    setSearchParams,
  ]);

  const debouncedSearch = useDebounce(state.search);

  const { data, isError, isLoading } = useSearch(
    debouncedSearch,
    state.page,
    state.pageSize,
    state.position,
    state.radius,
    state.selectedCountry,
    state.currentAdmin,
  );

  if (isError) {
    return (
      <div className={'flex justify-center items-center w-full min-h-[100vh] bg-charcoal'}>
        <p className={'text-red-500 text-3xl'} data-testid={'error'}>
          Error 😞
        </p>
      </div>
    );
  }

  const onPreviousPage = () => {
    setCurrentState({ page: state.page - 1 <= 0 ? 1 : state.page - 1 });
  };

  const onNextPage = () => {
    const pagination = data.pagination;
    setCurrentState({
      page: state.page + 1 >= pagination.totalNumberOfPages ? pagination.totalNumberOfPages : state.page + 1,
    });
  };

  return (
    <div data-testid='search' className='text-center bg-charcoal p-10'>
      {
        <div className='bg-charcoal flex flex-col items-center justify-center h-[100vh]'>
          <SearchBar
            filters={{
              search: state.search,
              cities: state.showResults && data.cities !== undefined ? [] : data.cities?.map((c: FrontCity) => c.city.name),
              countries: data.filters.countries,
              selectedCountry: state.selectedCountry,
              currentPageSize: state.pageSize,
              currentRadius: state.radius,
            }}
            isLoading={isLoading}
            onRadiusChange={(radius: number) => {
              setCurrentState({ radius });
            }}
            onPageSizeChange={(pageSize: number) => {
              setCurrentState({ pageSize });
            }}
            onSearchTextChange={(search) => {
              setCurrentState({ search, showResults: false, selectedCountry: null });
            }}
            onSearchButtonClicked={() => {
              setCurrentState({ filterResult: false, showResults: true });
            }}
            onCountryChange={(country) => {
              setCurrentState({ selectedCountry: country });
            }}
            onSuggestionsClicked={(search) => {
              setCurrentState({ search, filterResult: true, showResults: true });
            }}
            onLocationAsked={() =>
              askLocation((position) => {
                setCurrentState({
                  position: position,
                  showResults: false,
                });
              })
            }
          />
          {debouncedSearch.length > 0 && (
            <Results
              cities={data.cities}
              pagination={data.pagination}
              onPrevious={onPreviousPage}
              onNext={onNextPage}
            />
          )}
        </div>
      }
    </div>
  );
};

export default Search;
