import Search from './Search';
import { render, fireEvent, waitFor } from '@testing-library/react';
import React from 'react';
import { useQuery } from '@tanstack/react-query';

import '@testing-library/jest-dom'
jest.mock('@tanstack/react-query', () => ({
    useQuery: jest.fn(),
}));

const mockedUseQuery = useQuery as jest.MockedFn<typeof useQuery>;

describe('Search', () => {
    const original = jest.requireActual("@tanstack/react-query");
    beforeEach(() => {
        mockedUseQuery.mockReset();
    });

    it('should render correctly', async () => {
        mockedUseQuery.mockReturnValue({
            ...original,
            isLoading: false,
            error: null,
            isError: false,
            data: { cities: [{ id: 1, ascii: 'Test', name: 'Test' }] },
        });

        const { getByTestId } = render(<Search />);

        expect(getByTestId('search-bar')).toBeInTheDocument();
    });

    it('should render loading state', async () => {
        mockedUseQuery.mockReturnValue({
            ...original,
            isLoading: true,
            error: null,
            isError: false,
            data: {
                cities: [{ id: 1, ascii: 'Test', name: 'Test' }],
            },
        });

        const { getByTestId, } = render(<Search />);

        expect(getByTestId('search-button').firstChild).toHaveClass('animate-spin');
    });

    it('should render error state', async () => {
        mockedUseQuery.mockReturnValue({
            ...original,
            isLoading: false,
            error: new Error('Test error'),
            isError: true,
            data: undefined,
        });

        const { getByTestId } = render(<Search />);

        expect(getByTestId('error')).toBeInTheDocument();
    });
});