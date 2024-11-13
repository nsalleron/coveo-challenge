import { isNaN } from 'lodash';

export type LatLngCoords = { latitude: number; longitude: number };

export enum URL_SEARCH_PARAMS {
  QUERY = 'query',
  LATITUDE = 'latitude',
  LONGITUDE = 'longitude',
  SHOW = 'show',
  FILTER = 'filter',
  PAGE = 'page',
  PAGE_SIZE = 'page_size',
  RADIUS = 'radius',
  COUNTRY = 'country',
  ADMIN = 'admin',
}

export const retrievePositionFromSearchParams = (latitude: string | null, longitude: string | null) => {
  try {
    if (latitude && longitude) {
      const lat = parseFloat(latitude);
      const long = parseFloat(longitude);
      if (isNaN(latitude) || isNaN(long)) {
        return null;
      }
      return { latitude: lat, longitude: long };
    }
  } catch {
    console.error('Failed to parse location: latitude or longitude is not a number');
  }
  return null;
};

export const retrievePageFromSearchParams = (page: string | null, defaultValue?: number | null) => {
  if (page) {
    const parsedNumber = parseInt(page);
    return isNaN(parsedNumber) ? (defaultValue ?? 1) : parsedNumber;
  }
  return defaultValue ?? 1;
};

export const updateSearchParams = (searchParams: URLSearchParams, name: string, value: string | null) => {
  if (searchParams.has(name) && value !== null) {
    searchParams.set(name, value);
  } else if (value !== null) {
    searchParams.append(name, value);
  } else {
    searchParams.delete(name);
  }
};
