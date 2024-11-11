import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();
export const wrapper = (children: React.ReactNode) => {
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
};
