import {
  ADD_FRUIT_ACTION,
  DELETE_FRUIT_ACTION,
  FRUITS_LOADED,
  FRUITS_LOADED_FAILED
} from "../constants";

type ActionProps = {
    type: string,
    payload: any
}

type StateProps = {
    fruitsList: any[];
    loaded: boolean;
    loadError?: string;
}

const FruitsReducer = (state: StateProps, action: ActionProps) => {

    switch(action.type) {
        case FRUITS_LOADED:
            return {fruitsList: [...state.fruitsList, ...action.payload], loaded: true};
        case FRUITS_LOADED_FAILED:
            return {fruitsList: [...state.fruitsList], loaded: true, loadError: action.payload.loadError};
        case ADD_FRUIT_ACTION:
            return {fruitsList: [...state.fruitsList, action.payload], loaded: true};
        case DELETE_FRUIT_ACTION:
            return {fruitsList : state.fruitsList.length ? state.fruitsList.filter((d) => d.cells?.[0]?.title !== action.payload.name) : [], loaded: true}
        default:
            return state;
    }
}

export default FruitsReducer;