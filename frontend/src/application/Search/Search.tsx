import React from 'react';
import SearchBar from './components/SearchBar/SearchBar';
import { City, Results } from './components/Results/Results';
import useObjectState from '../hooks/useObjectState';
import { useQuery } from '@tanstack/react-query';
import useDebounce from '../hooks/useDebounce';
import { askLocation } from '../utils/location';

type SearchState = {
  search: string;
  position: Coordinates | null;
  filterResult: boolean;
  showResults: boolean;
  page: number;
};
const initialData = {
  cities: [] as City[],
  totalNumberOfPages: 0,
  page: 0,
};

function useSearch(currentPage: number, position: Coordinates | null, search: string) {
  return useQuery({
    queryKey: ['query', search, currentPage],
    initialData: initialData,
    queryFn: async () => {
      if (search.length === 0) {
        return initialData;
      }

      const q = search.toLowerCase();
      const path =
        position === null
          ? `${process.env.REACT_APP_API_URL}/suggestions?q=${q}&page=${currentPage - 1}`
          : `${process.env.REACT_APP_API_URL}/suggestions?q=${q}&latitude=${position.latitude}&longitude=${position.longitude}&page=${currentPage - 1}`;

      const { cities, totalNumberOfPages, page } = await fetch(path).then((response) => response.json());

      return { cities, totalNumberOfPages, page: page + 1 };
    },
  });
}

const Search: React.FunctionComponent = () => {
  const [state, setCurrentState] = useObjectState<SearchState>({
    search: '',
    position: null,
    filterResult: false,
    showResults: false,
    page: 1,
  });

  const debouncedSearch = useDebounce(state.search);

  const { data, isError, isLoading } = useSearch(state.page, state.position, debouncedSearch);

  const isResultDisplayable = state.showResults && debouncedSearch === state.search;

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
            onSearchTextChange={(search) => setCurrentState({ search, showResults: false })}
            onSearchButtonClicked={() => {
              setCurrentState({ filterResult: false, showResults: true });
            }}
            onSuggestionsClicked={(search) => {
              setCurrentState({ search, filterResult: true, showResults: true });
            }}
            cities={state.showResults ? [] : data.cities.map((c: City) => c.name)}
            onLocationAsked={() =>
              askLocation((position) =>
                setCurrentState({
                  position: position,
                  showResults: false,
                }),
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
