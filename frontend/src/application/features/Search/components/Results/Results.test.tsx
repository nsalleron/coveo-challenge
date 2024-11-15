import {Results, ResultsProps} from './Results';
import {fireEvent, render} from '@testing-library/react';
import React from 'react';
import '@testing-library/jest-dom';

describe('Search', () => {
    const setup = (props: Omit<ResultsProps, 'onNext' | 'onPrevious'>) => {
        const handleNextPageClick = jest.fn();
        const handlePreviousPageClick = jest.fn();
        const resultList = <Results onNext={handleNextPageClick} onPrevious={handlePreviousPageClick} {...props} />;
        const {getByTestId} = render(resultList);

        return {
            handleNextPageClick,
            handlePreviousPageClick,
            getByTestId,
        };
    };
    it('should displays empty state when there are no cities', async () => {
        const {getByTestId} = setup({
            pagination: {
                totalNumberOfPages: 2,
                page: 1,
            },
            cities: [],
        });

        expect(getByTestId('empty-state')).toBeInTheDocument();
    });

    it('should displays a city when there is at least one city', async () => {
        const {getByTestId} = setup({
            pagination: {
                totalNumberOfPages: 2,
                page: 1,
            },
            cities: [{
                score: 1,
                city: {id: 0, country: 'aCountry', name: 'aName', altNames: ['aAltName'], admins: ['']}
            }],
        });

        expect(getByTestId('city-0')).toBeInTheDocument();
    });

    it('should allows clicking on the next page', async () => {
        const {handleNextPageClick, handlePreviousPageClick, getByTestId} = setup({
            pagination: {
                totalNumberOfPages: 2,
                page: 1,
            },
            cities: [{
                score: 1,
                city: {id: 0, country: 'aCountry', name: 'aName', altNames: ['aAltName'], admins: ['']}
            }],
        });

        fireEvent.click(getByTestId('arrow-right'));

        expect(handleNextPageClick).toHaveBeenCalledTimes(1);
        expect(handlePreviousPageClick).not.toHaveBeenCalled();
    });

    it('should allows clicking on the previous page', async () => {
        const {handleNextPageClick, handlePreviousPageClick, getByTestId} = setup({
            pagination: {
                totalNumberOfPages: 2,
                page: 1,
            },
            cities: [{
                score: 1,
                city: {id: 0, country: 'aCountry', name: 'aName', altNames: ['aAltName'], admins: ['']}
            }],
        });

        fireEvent.click(getByTestId('arrow-left'));

        expect(handleNextPageClick).not.toHaveBeenCalled();
        expect(handlePreviousPageClick).toHaveBeenCalledTimes(1);
    });
});
