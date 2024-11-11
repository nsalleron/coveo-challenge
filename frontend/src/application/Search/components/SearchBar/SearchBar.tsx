import React, {useRef} from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { faLocationCrosshairs, faMagnifyingGlass, faSpinner } from '@fortawesome/free-solid-svg-icons'

export interface SearchBarProps {
    placeholder: string,
    currentSearch: string,
    onSearchButtonClicked: () => void
    onSearchTextChange: (query: string) => void
    onSuggestionsClicked: (query: string) => void
    cities: string[]
    onLocationAsked: () => void
    isLoading: boolean
}

const SearchBar: React.FunctionComponent<SearchBarProps> = ({
                                                                placeholder,
                                                                currentSearch,
                                                                onSearchButtonClicked,
                                                                onSuggestionsClicked,
                                                                onSearchTextChange,
                                                                onLocationAsked,
                                                                cities,
                                                                isLoading
                                                            }) => {
    const inputRef = useRef<HTMLInputElement>(null);

    const onKeyDown = (e: any) => {
        if (e.key === 'Enter' && inputRef.current != null) {
            onSearchButtonClicked();
        }
    }

    return <div data-testid="search-bar" className="flex">
        <div className={"flex flex-col"}>
            <input
                data-testid="search-input"
            className={"max-w-lg text-xl p-3 border-none"}
            ref={inputRef}
            placeholder={placeholder}
            value={currentSearch}
            onKeyDown={onKeyDown}
            onChange={(e) => onSearchTextChange(e.target.value)}>
        </input>
            {cities.length > 0 &&
                <ol data-testid="suggestions" className="text-left text-black max-w-lg border-t-gray-500 border border-solid">
                    {cities.map((city, key) => {
                        return <li key={key}
                                    className={"bg-gray-200 cursor-pointer text-xl pt-1.5 pb-1.5 pr-0 pl-3 hover:bg-gray-300"}
                                    onClick={() => {
                                        onSuggestionsClicked(city)
                                    }}>
                            <span>{city}</span>
                        </li>;
                    })}
                </ol>
            }</div>

        <div className={"flex ml-5 gap-2"}>
            <button
                data-testid="location-button"
                className={"bg-gray-300 w-[52px] h-[52px] text-xl decoration-0 cursor-pointer p-3 border-none"}
                aria-label={"locate me"} onClick={onLocationAsked}>
                <FontAwesomeIcon icon={faLocationCrosshairs} />
            </button>
            <button
                data-testid="search-button"
                className={"bg-gray-300 w-[52px] h-[52px] text-xl decoration-0 cursor-pointer p-3 border-none"}
                aria-label={"search"} onClick={() => {
                if (inputRef.current != null) {
                    onSearchButtonClicked();
                } else {
                    onSearchTextChange("");
                }
            }}>
                <FontAwesomeIcon icon={!isLoading ? faMagnifyingGlass : faSpinner } className={isLoading ? 'animate-spin' : '' }/>
            </button>
        </div>


    </div>
}

export default SearchBar;