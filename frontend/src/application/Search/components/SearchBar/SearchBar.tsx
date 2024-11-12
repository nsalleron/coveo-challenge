import React, {useRef, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {faLocationCrosshairs, faMagnifyingGlass, faSpinner} from '@fortawesome/free-solid-svg-icons';
import {toNumber, toString} from 'lodash';
import DropdownMenu from "../../../components/DropdownMenu";

const radiusValues = [5, 10, 20, 40, 100];
const pageValues = [5, 10, 20, 40, 100];

export interface SearchBarProps {
    placeholder: string,
    currentSearch: string,
    onSearchButtonClicked: () => void,
    onSearchTextChange: (query: string) => void,
    onSuggestionsClicked: (query: string) => void,
    cities: string[],
    isLoading: boolean,
    onLocationAsked: () => void,
    onRadiusChange?: (radius: number) => void,
    onPageSizeChange?: (radius: number) => void,
    showRadius?: boolean
}

type LocationWithRadiusProps = {
    onLocationAsked: () => void,
    onRadiusChange?: (radius: number) => void,
    showRadius?: boolean
};

const LocationWithRadius = ({onLocationAsked, onRadiusChange, showRadius = false}: LocationWithRadiusProps) => {
    const [radiusVisible, setRadiusVisible] = useState(showRadius);

    return (
        <div className={'flex flex-col'}>
            <button
                data-testid='location-button'
                className={'bg-gray-300 min-w-20 h-14 text-xl decoration-0 cursor-pointer p-3 border-none'}
                aria-label={'locate me'}
                onClick={() => {
                    onLocationAsked();
                    setRadiusVisible(true);
                }}
            >
                <FontAwesomeIcon icon={faLocationCrosshairs}/>
            </button>
            {radiusVisible && (
                <DropdownMenu
                    title={'Radius'}
                    unit={'km'}
                    values={radiusValues.map(toString)}
                    callback={(value) => onRadiusChange?.(toNumber(value))}
                />
            )}
        </div>
    );
};

const SearchBar: React.FunctionComponent<SearchBarProps> = ({
                                                                placeholder,
                                                                currentSearch,
                                                                onSearchButtonClicked,
                                                                onSuggestionsClicked,
                                                                onSearchTextChange,
                                                                onLocationAsked,
                                                                cities,
                                                                isLoading,
                                                                onRadiusChange,
                                                                onPageSizeChange,
                                                                showRadius = false
                                                            }) => {
    const inputRef = useRef<HTMLInputElement>(null);
    const onKeyDown = (e: any) => {
        if (e.key === 'Enter' && inputRef.current != null) {
            onSearchButtonClicked();
        }
    };

    return (
        <div data-testid='search-bar' className='flex'>
            <div className={'flex flex-col'}>
                <input
                    data-testid='search-input'
                    className={'max-w-lg text-xl p-3 border-none h-14'}
                    ref={inputRef}
                    placeholder={placeholder}
                    value={currentSearch}
                    onKeyDown={onKeyDown}
                    onChange={(e) => onSearchTextChange(e.target.value)}
                ></input>
                {cities.length > 0 && (
                    <ol data-testid='suggestions'
                        className='text-left text-black max-w-lg border-t-gray-500 border border-solid'>
                        {cities.map((city, key) => {
                            return (
                                <li
                                    key={key}
                                    className={'bg-gray-200 cursor-pointer text-xl pt-1.5 pb-1.5 pr-0 pl-3 hover:bg-gray-300'}
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

            <div className={'flex ml-5 gap-2'}>
                <LocationWithRadius onLocationAsked={onLocationAsked} onRadiusChange={onRadiusChange}
                                    showRadius={showRadius}/>
                <div className={"flex flex-col"}>
                    <button
                        data-testid='search-button'
                        className={'bg-gray-300 h-14 text-xl decoration-0 cursor-pointer p-3 border-none'}
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
                        values={pageValues.map(toString)}
                        callback={(value) => onPageSizeChange?.(toNumber(value))}
                    />
                </div>
            </div>
        </div>
    );
};

export default SearchBar;
