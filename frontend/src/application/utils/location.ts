export const askLocation = (setPosition: (coordinates: Coordinates) => void) => {
  navigator.geolocation.getCurrentPosition((p) => {
    setPosition(p.coords);
  });
};
