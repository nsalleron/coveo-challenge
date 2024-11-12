import {render,} from '@testing-library/react';
import React from 'react';

import '@testing-library/jest-dom';
import ErrorPage from "./error-page";

describe('Error-page', () => {
    it('should render correctly', async () => {
        const {getByTestId} = render(<ErrorPage/>);

        expect(getByTestId('error-page')).toBeInTheDocument();
    });

});
