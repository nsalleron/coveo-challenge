import React, { ReactNode, useState } from 'react';
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';

type DropdownMenuProps<T extends ReactNode> = {
  title: string;
  unit?: string;
  values: T[];
  selectedValue: T | null;
  callback?: (value: T | null) => void;
};

function DropdownMenu<T extends ReactNode>({ title, unit, values, selectedValue, callback }: DropdownMenuProps<T>) {
  const [currentTitle, setCurrentTitle] = useState(title);

  return (
    <Menu>
      <MenuButton className='flex justify-center items-center gap-2 h-14 p-3 rounded-2xl bg-gray-300 text-sm/6 font-semibold shadow-inner shadow-white/10 focus:outline-none data-[hover]:bg-gray-400 data-[open]:bg-gray-400 data-[focus]:outline-1 data-[focus]:outline-white'>
        {currentTitle}
      </MenuButton>

      <MenuItems
        transition
        anchor='bottom end'
        className='w-1/5 origin-top-right rounded-xl border border-white/5 p-1 text-sm/6 text-white transition duration-100 ease-out [--anchor-gap:var(--spacing-1)] focus:outline-none data-[closed]:scale-95 data-[closed]:opacity-0 bg-charcoal'
      >
        {values.map((value, key) => {
          return (
            <MenuItem key={key}>
              <button
                onClick={() => {
                    const titleValue = value !== null ? value : ''
                    const titleUnit = unit !== undefined ? unit : ''
                  if (selectedValue !== value) {
                    setCurrentTitle(`${title} ${titleValue} ${titleUnit}`);
                    return callback?.(value);
                  } else {
                    setCurrentTitle(`${title}`);
                    return callback?.(null);
                  }
                }}
                className='groupflex w-full items-center gap-2 rounded-lg py-1.5 px-1 data-[focus]:bg-white/10'
              >
                {value !== null ? value : 'Remove'} {unit}
              </button>
            </MenuItem>
          );
        })}
      </MenuItems>
    </Menu>
  );
}

export default DropdownMenu;
