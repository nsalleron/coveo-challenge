import * as React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faArrowRight } from '@fortawesome/free-solid-svg-icons';

export interface City {
  id: number;
  country: string;
  name: string;
}

type ResultsProps = {
  totalNumberOfPages: number;
  currentPage: number;
  cities: City[];
  onNext: () => void;
  onPrevious: () => void;
};
export const Results: React.FunctionComponent<ResultsProps> = ({
  totalNumberOfPages,
  currentPage,
  cities,
  onNext,
  onPrevious,
}) => {
  if (cities.length === 0) {
    return (
      <p data-testid={'empty-state'} className={'text-white pt-6'}>
        No cities available
      </p>
    );
  }

  return (
    <div data-testid='result-list' className='w-full'>
      <ol className={'m-6'}>
        {cities.map((c, i) => (
          <CityListItem key={i} city={c} />
        ))}
      </ol>
      <div className={'flex text-white justify-between items-center mx-10'}>
        <button data-testid='arrow-left' onClick={onPrevious}>
          <FontAwesomeIcon icon={faArrowLeft} height={24} width={24} />
        </button>
        <div data-testid='count-pages'> {`${currentPage} / ${totalNumberOfPages}`} </div>

        <button data-testid='arrow-right' onClick={onNext}>
          <FontAwesomeIcon icon={faArrowRight} height={24} width={24} />
        </button>
      </div>
    </div>
  );
};

type CityListItemProps = {
  city: City;
};

const CityListItem: React.FunctionComponent<CityListItemProps> = ({ city }) => (
  <li
    data-testid={`city-${city.id}`}
    key={`city-${city.id}`}
    id={'suggestions'}
    className='flex p-2.5 bg-orange-600 border border-black'
  >
    <img src='/images/city.webp' className='h-24 w-24 float-left' alt={`${city.name} city logo`} />
    <div className='flex flex-col justify-center items-center flex-grow'>
      <span>Country: {city.country}</span>
      <span>Name: {city.name}</span>
    </div>
  </li>
);
