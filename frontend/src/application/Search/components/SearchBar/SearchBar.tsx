import React, { useRef, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { faLocationCrosshairs, faMagnifyingGlass, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { toNumber } from 'lodash';
import DropdownMenu from '../../../components/DropdownMenu';
import { Country } from '../../useSearchApi';

const radiusValues = [null, 5, 10, 20, 40, 100];
const pageValues = [null, 5, 10, 20, 40, 100];

type LocationWithRadiusProps = {
  onLocationAsked: () => void;
  onRadiusChange?: (radius: number) => void;
  currentRadius?: number;
  showRadius?: boolean;
};

const LocationWithRadius = ({
  onLocationAsked,
  onRadiusChange,
  currentRadius,
  showRadius = false,
}: LocationWithRadiusProps) => {
  const [radiusVisible, setRadiusVisible] = useState(showRadius);

  return (
    <div className={'flex flex-col gap-2'}>
      <button
        data-testid='location-button'
        className={'bg-gray-300 min-w-20 h-14 text-xl decoration-0 cursor-pointer p-3 border-none rounded-2xl'}
        aria-label={'locate me'}
        onClick={() => {
          onLocationAsked();
          setRadiusVisible(true);
        }}
      >
        <FontAwesomeIcon icon={faLocationCrosshairs} />
      </button>
      {radiusVisible && (
        <DropdownMenu
          title={'Radius'}
          unit={'km'}
          values={radiusValues}
          callback={(value) => onRadiusChange?.(toNumber(value))}
          selectedValue={currentRadius}
        />
      )}
    </div>
  );
};

export interface SearchBarProps {
  placeholder: string;
  currentSearch: string;
  onSearchButtonClicked: () => void;
  onSearchTextChange: (query: string) => void;
  onSuggestionsClicked: (query: string) => void;
  cities: string[];
  isLoading: boolean;
  onLocationAsked: () => void;
  currentRadius?: number;
  onRadiusChange?: (radius: number) => void;
  currentPage: number;
  onPageSizeChange?: (radius: number) => void;
  showRadius?: boolean;
  selectedCountry: string | null;
  onCountryChange?: (country: string | null) => void;
  countries: Country[];
}

const SearchBar: React.FunctionComponent<SearchBarProps> = ({
  placeholder,
  currentSearch,
  onSearchButtonClicked,
  onSuggestionsClicked,
  onSearchTextChange,
  onLocationAsked,
  cities,
  isLoading,
  currentRadius,
  onRadiusChange,
  currentPage,
  onPageSizeChange,
  showRadius = false,
  selectedCountry,
  onCountryChange,
  countries,
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
        <input
            data-testid='search-input'
            className={'max-w-lg text-xl p-3 border-none h-14 rounded-2xl'}
            ref={inputRef}
            placeholder={placeholder}
            value={currentSearch}
            onKeyDown={onKeyDown}
            onChange={(e) => onSearchTextChange(e.target.value)}
        ></input>
        {cities.length > 0 && (
            <ol data-testid='suggestions' className='text-left mt-1 text-black max-w-lg border-solid rounded-2xl'>
              {cities.map((city, key) => {
                const isFirst = key === 0 ? "rounded-t-2xl" : ""
                const isLast = key === cities.length - 1 ? "rounded-b-2xl" : ""
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
        <div>
          <DropdownMenu
              title={'Countries'}
              selectedValue={selectedCountry}
              values={countries.map((country) => country.name.toString())}
              callback={(value) => onCountryChange?.(value)}
          />
        </div>
      </div>

      <div className={'flex ml-5 gap-2'}>
        <LocationWithRadius
            onLocationAsked={onLocationAsked}
            onRadiusChange={onRadiusChange}
            showRadius={showRadius}
            currentRadius={currentRadius}
        />
        <div className={'flex flex-col gap-2'}>
          <button
            data-testid='search-button'
            className={'bg-gray-300 h-14 text-xl decoration-0 cursor-pointer p-3 border-none rounded-2xl'}
            aria-label={'search'}
            onClick={() => {
              if (inputRef.current != null) {
                onSearchButtonClicked();
              } else {
                onSearchTextChange('');
              }
            }}
          >
            <FontAwesomeIcon
              icon={!isLoading ? faMagnifyingGlass : faSpinner}
              className={isLoading ? 'animate-spin' : ''}
            />
          </button>
          <DropdownMenu
            title={'Page size'}
            values={pageValues}
            callback={(value) => onPageSizeChange?.(toNumber(value))}
            selectedValue={currentPage}
          />
        </div>
      </div>
    </div>
  );
};

export default SearchBar;
