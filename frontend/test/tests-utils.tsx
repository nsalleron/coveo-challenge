import { MemoryRouter } from 'react-router-dom';

export const withMemoryRouter = (child: JSX.Element, initialEntries?: string[]): JSX.Element => (
  <MemoryRouter
    basename='/'
    future={{
      v7_startTransition: true,
      v7_relativeSplatPath: true,
    }}
    initialEntries={initialEntries?.map((e) => {
      if (e.startsWith('/')) {
        return e;
      }

      return `/${e}`;
    })}
  >
    {child}
  </MemoryRouter>
);
