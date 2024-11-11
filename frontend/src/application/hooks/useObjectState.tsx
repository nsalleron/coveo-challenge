import { useCallback, useState } from 'react';
import {cloneDeep} from "lodash";

export type SetState<T> = (state: T) => void;

type UseObjectState<T> = [T, SetState<Partial<T>>];

const useObjectState = <T, >(initialState: T): UseObjectState<T> => {
  const [state, setState] = useState<T>(initialState);

  const setObjectState = useCallback((newState: Partial<T>) => {
    setState((prevState) => ({
      ...cloneDeep(prevState),
      ...cloneDeep(newState),
    }));
  }, []);

  return [state, setObjectState];
};

export default useObjectState;
