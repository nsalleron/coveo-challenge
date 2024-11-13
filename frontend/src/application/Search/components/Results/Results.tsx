import * as React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { FrontCity, Pagination} from '../../useSearchApi';

export type ResultsProps = {
  pagination: Pagination;
  cities: FrontCity[];
  onNext: () => void;
  onPrevious: () => void;
};
export const Results: React.FunctionComponent<ResultsProps> = ({ cities, pagination, onNext, onPrevious }) => {
  if (cities.length === 0) {
    return (
      <p data-testid={'empty-state'} className={'text-white pt-6'}>
        No cities available
      </p>
    );
  }

  return (
    <div data-testid='result-list' className='w-full'>
      <ol className={'flex flex-col m-6 gap-2'}>
        {cities.map((c, i) => (
          <CityListItem key={i} cityWithScore={c} />
        ))}
      </ol>
      <div className={'flex text-white justify-between items-center mx-10'}>
        <button data-testid='arrow-left' onClick={onPrevious}>
          <FontAwesomeIcon icon={faArrowLeft} height={24} width={24} />
        </button>
        <div data-testid='count-pages'> {`${pagination.page} / ${pagination.totalNumberOfPages}`} </div>

        <button data-testid='arrow-right' onClick={onNext}>
          <FontAwesomeIcon icon={faArrowRight} height={24} width={24} />
        </button>
      </div>
    </div>
  );
};

type CityListItemProps = {
  cityWithScore: FrontCity;
};

const CityListItem: React.FunctionComponent<CityListItemProps> = ({ cityWithScore }) => {
  let { city, score } = cityWithScore;
  let admins = city.admins.join(', ');

  return (
    <li
      data-testid={`city-${city.id}`}
      key={`city-${city.id}`}
      id={'suggestions'}
      className='flex p-2.5 bg-orange-600 border border-black rounded-2xl'
    >
      <img src='/images/city.webp' className='h-24 w-24 float-left' alt={`${city.name} city logo`} />
      <div className='flex justify-between px-10 items-start flex-grow'>
        <div className={'flex flex-col items-start w-1/2'}>
          <span>Search Score: {score}</span>
          <span>Name: {city.name}</span>
          <span>Country: {city.country}</span>
          <span>Admins: {admins.substr(0, admins.length - 2)}</span>
        </div>
        {city.altNames.length > 0 && (
          <div className={'flex flex-col justify-start items-start w-1/2 gap-2'}>
            <span>Alternative names</span>
            <ol className={'flex flex-wrap gap-2 items-start overflow-y-auto max-h-16 w-full border-solid'}>
              {city.altNames.map((altName, id) => (
                <li
                  className={'rounded-md bg-charcoal py-0.5 px-2.5 border text-xs text-white transition-all shadow-sm'}
                  key={id}
                >
                  {altName}
                </li>
              ))}
            </ol>
          </div>
        )}
      </div>
    </li>
  );
};
