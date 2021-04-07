import React from 'react';
import {
  FRUIT_SEASON_OPTIONS,
  FRUITS_LOADED,
  FRUITS_LOADED_FAILED
} from '../../constants';
import FruitsReducer from '../../reducers/FruitsReducer';

export type dispatchProps = ({ type, payload }: { type: string, payload: any }) => void

type FruitsContextProps = {
  state: { fruitsList: any[], loaded: boolean, loadError?: string };
  dispatch: dispatchProps;
}

export const FruitsContext = React.createContext({} as FruitsContextProps);

const FruitsProvider: React.FC = (props) => {
  const [state, dispatch] = React.useReducer(FruitsReducer, { fruitsList: [], loaded: false });

  const getSeasonLabel = (val: string) => {
    const season = FRUIT_SEASON_OPTIONS.find(s => s.value === val)
    //console.log("LABEL %s", season?.label);
    return season?.label
  }
  React.useEffect(() => {
    fetch(`${process.env.REACT_APP_API_URL}/fruits`)
      .then(async (resp) => {
        const data = await resp.json()
        if (resp.ok) {
          //console.log("Fruits Data %s", JSON.stringify(data));
          const rowsData = data.map((d: any) => {
            return {
              cells: [
                { title: d.name },
                { title: getSeasonLabel(d.season) },
                { title: "delete", fruitname: d.name }
              ]
            }
          });
          dispatch({
            type: FRUITS_LOADED,
            payload: rowsData
          })
        } else {
          //console.log("Error %s", JSON.stringify(data));
          const payload: any = {};
          if (data.status === 500) {
            payload.loaded = true;
            payload.loadError = "Some Error occurred fetching the fruits";
          } else {
            payload.loaded = true;
            payload.loadError = `Error Code:${data.status}, Error: ${data.message}`;
          }
          dispatch({
            type: FRUITS_LOADED_FAILED,
            payload
          })
        }
      })
      .catch((err) => {
        console.log('Error Retrieving Fruits: %s', err);
      })
    // eslint-disable-next-line
  }, [])

  const value = { state, dispatch }

  return (
    <FruitsContext.Provider value={value}>
      {props.children}
    </FruitsContext.Provider>
  )
}

export default FruitsProvider;
