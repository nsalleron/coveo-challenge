import React, { useRef } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { faLocationCrosshairs, faMagnifyingGlass, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { toNumber } from 'lodash';
import DropdownMenu from '../../../../components/DropdownMenu';
import { Filter } from '../../useSearchApi';

const radiusValues = [null, 5, 10, 20, 40, 100];
const pageValues = [null, 5, 10, 20, 40, 100];

type LocationWithRadiusProps = {
  onLocationAsked: () => void;
  onRadiusChange?: (radius: number) => void;
  currentRadius?: number;
};

const LocationWithRadius = ({ onLocationAsked, onRadiusChange, currentRadius }: LocationWithRadiusProps) => {
  return (
    <div className={'flex flex-col gap-2'}>
      <button
        data-testid='location-button'
        className={'bg-gray-300 min-w-20 h-14 text-xl decoration-0 cursor-pointer p-3 border-none rounded-2xl'}
        aria-label={'locate me'}
        onClick={() => {
          onLocationAsked();
        }}
      >
        <FontAwesomeIcon icon={faLocationCrosshairs} />
      </button>
      <DropdownMenu
        title={'Radius'}
        unit={'km'}
        values={radiusValues}
        onChange={(value) => onRadiusChange?.(toNumber(value))}
        selectedValue={currentRadius}
      />
    </div>
  );
};

export type SearchBarProps = {
  filters: {
    search: string;
    cities: string[];
    countries: Filter[];
    selectedCountry: string | null;
    currentPageSize: number;
    currentRadius?: number;
  };
  isLoading: boolean;
  onSearchButtonClicked: () => void;
  onSearchTextChange: (query: string) => void;
  onSuggestionsClicked: (query: string) => void;
  onLocationAsked: () => void;
  onRadiusChange: (radius: number) => void;
  onPageSizeChange: (radius: number) => void;
  onCountryChange: (country: string | null) => void;
};

type SearchWithPageSizeProps = {
  onSearchButtonClicked: () => void;
  isLoading: boolean;
  onPageSizeChange: (value: number | null) => void;
  selectedValue: number;
};
const SearchWithPageSize: React.FC<SearchWithPageSizeProps> = ({
  onSearchButtonClicked,
  isLoading,
  onPageSizeChange,
  selectedValue,
}) => {
  return (
    <div className={'flex flex-col gap-2'}>
      <button
        data-testid='search-button'
        className={'bg-gray-300 h-14 text-xl decoration-0 cursor-pointer p-3 border-none rounded-2xl'}
        aria-label={'search'}
        onClick={onSearchButtonClicked}
      >
        <FontAwesomeIcon
          icon={!isLoading ? faMagnifyingGlass : faSpinner}
          className={isLoading ? 'animate-spin' : ''}
        />
      </button>
      <DropdownMenu title={'Page size'} values={pageValues} onChange={onPageSizeChange} selectedValue={selectedValue} />
    </div>
  );
};

const SearchBar: React.FunctionComponent<SearchBarProps> = ({
  filters,
  isLoading,
  onSearchButtonClicked,
  onSuggestionsClicked,
  onSearchTextChange,
  onLocationAsked,
  onRadiusChange,
  onPageSizeChange,
  onCountryChange,
}) => {
  const inputRef = useRef<HTMLInputElement>(null);

  const onKeyDown = (e: any) => {
    if (e.key === 'Enter' && inputRef.current != null) {
      onSearchButtonClicked();
    }
  };

  return (
    <div data-testid='search-bar' className='flex gap-2 items-start'>
      <div className={'flex gap-2'}>
        <div className={'flex flex-col'}>
          <input
            data-testid='search-input'
            className={'max-w-lg text-xl p-3 border-none h-14 rounded-2xl'}
            ref={inputRef}
            placeholder={'Search cities'}
            value={filters.search}
            onKeyDown={onKeyDown}
            onChange={(e) => onSearchTextChange(e.target.value)}
          ></input>
          {filters.cities.length > 0 && (
            <ol data-testid='suggestions' className='text-left mt-1 text-black max-w-lg border-solid rounded-2xl'>
              {filters.cities.map((city, key) => {
                const isFirst = key === 0 ? 'rounded-t-2xl' : '';
                const isLast = key === filters.cities.length - 1 ? 'rounded-b-2xl' : '';
                return (
                  <li
                    key={key}
                    className={`bg-gray-200 cursor-pointer text-xl pt-1.5 pb-1.5 pr-0 pl-3 hover:bg-gray-300 ${isFirst} ${isLast}`}
                    onClick={() => {
                      onSuggestionsClicked(city);
                    }}
                  >
                    <span>{city}</span>
                  </li>
                );
              })}
            </ol>
          )}
        </div>
        <DropdownMenu
          title={'Countries'}
          selectedValue={filters.selectedCountry}
          values={filters.countries.map((country) => country.name.toString())}
          onChange={(value) => onCountryChange?.(value)}
        />
      </div>

      <div className={'flex ml-5 gap-2'}>
        <LocationWithRadius
          onLocationAsked={onLocationAsked}
          onRadiusChange={onRadiusChange}
          currentRadius={filters.currentRadius}
        />
        <SearchWithPageSize
          onSearchButtonClicked={() => {
            if (inputRef.current != null) {
              onSearchButtonClicked();
            } else {
              onSearchTextChange('');
            }
          }}
          isLoading={isLoading}
          onPageSizeChange={(value) => onPageSizeChange?.(value === null ? 5 : toNumber(value))}
          selectedValue={filters.currentPageSize}
        />
      </div>
    </div>
  );
};

export default SearchBar;
