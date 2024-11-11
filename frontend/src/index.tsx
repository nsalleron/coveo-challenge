import React from 'react';
import './index.css';
import Search from './application/Search/Search';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const container = document.getElementById('root');
const root = createRoot(container!);

const queryClient = new QueryClient();

root.render(
  <QueryClientProvider client={queryClient}>
    <Search />
  </QueryClientProvider>,
);
