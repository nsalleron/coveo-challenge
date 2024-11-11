import React from 'react';
import { fireEvent, render } from '@testing-library/react';
import SearchBar from './SearchBar';
import '@testing-library/jest-dom';

describe('SearchBar', () => {
  const props = {
    placeholder: 'Search...',
    currentSearch: '',
    onSearchButtonClicked: jest.fn(),
    onSearchTextChange: jest.fn(),
    onSuggestionsClicked: jest.fn(),
    cities: ['New York', 'London'],
    onLocationAsked: jest.fn(),
    isLoading: false,
  };

  it('should renders correctly', () => {
    const { getByTestId } = render(<SearchBar {...props} />);
    expect(getByTestId('search-bar')).toBeInTheDocument();
    expect(getByTestId('search-input')).toBeInTheDocument();
    expect(getByTestId('location-button')).toBeInTheDocument();
    expect(getByTestId('search-button')).toBeInTheDocument();
  });

  it('should calls onSearchButtonClicked when search button is clicked', () => {
    const { getByTestId } = render(<SearchBar {...props} />);
    fireEvent.click(getByTestId('search-button'));
    expect(props.onSearchButtonClicked).toHaveBeenCalled();
  });

  it('should calls onSearchTextChange when text is changed', () => {
    const { getByTestId } = render(<SearchBar {...props} />);
    fireEvent.change(getByTestId('search-input'), { target: { value: 'New York' } });
    expect(props.onSearchTextChange).toHaveBeenCalledWith('New York');
  });

  it('should calls onLocationAsked when location button is clicked', () => {
    const { getByTestId } = render(<SearchBar {...props} />);
    fireEvent.click(getByTestId('location-button'));
    expect(props.onLocationAsked).toHaveBeenCalled();
  });

  it('should calls onSuggestionsClicked when a suggestion is clicked', () => {
    const { getByText } = render(<SearchBar {...props} />);
    fireEvent.click(getByText('New York'));
    expect(props.onSuggestionsClicked).toHaveBeenCalledWith('New York');
  });
});
