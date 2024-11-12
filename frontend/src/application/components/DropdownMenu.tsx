import React, {useState} from "react";
import {Menu, MenuButton, MenuItem, MenuItems} from "@headlessui/react";

function DropdownMenu({
                          title,
                          unit = '',
                          values,
                          callback,
                      }: {
    title: string;
    unit?: string;
    values: string[];
    callback?: (value: string) => void;
}) {
    const [currentTitle, setCurrentTitle] = useState(title)
    return (
        <Menu>
            <MenuButton
                className='flex items-center gap-2 rounded-md bg-gray-300 mt-2 p-2 text-sm/6 font-semibold shadow-inner shadow-white/10 focus:outline-none data-[hover]:bg-gray-400 data-[open]:bg-gray-400 data-[focus]:outline-1 data-[focus]:outline-white'>
                {currentTitle}
            </MenuButton>

            <MenuItems
                transition
                anchor='bottom end'
                className='w-20 origin-top-right rounded-xl border border-white/5 p-1 text-sm/6 text-white transition duration-100 ease-out [--anchor-gap:var(--spacing-1)] focus:outline-none data-[closed]:scale-95 data-[closed]:opacity-0 bg-charcoal '
            >
                {values.map((value, key) => {
                    return (
                        <MenuItem key={key}>
                            <button
                                onClick={() => {
                                    setCurrentTitle(`${title} ${value} ${unit}`)
                                    return callback?.(value);
                                }}
                                className='groupflex w-full items-center gap-2 rounded-lg py-1.5 px-1 data-[focus]:bg-white/10'
                            >
                                {value} {unit}
                            </button>
                        </MenuItem>
                    );
                })}
            </MenuItems>
        </Menu>
    );
}

export default DropdownMenu