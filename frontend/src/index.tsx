import React from 'react';
import './index.css';
import Search from './application/Search/Search';
import {createRoot} from 'react-dom/client';
import {QueryClient, QueryClientProvider} from '@tanstack/react-query';
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import "./index.css";
import ErrorPage from "./error-page";

const container = document.getElementById('root');
const root = createRoot(container!);

const queryClient = new QueryClient();


const router = createBrowserRouter([
    {
        path: "/",
        element: <Search/>,
        errorElement: <ErrorPage/>,
    },
]);

root.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router}/>
        </QueryClientProvider>,
    </React.StrictMode>
);
