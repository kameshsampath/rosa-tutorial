import * as React from 'react';
import {
  Alert,
  Button,
  Form,
  FormAlert,
  FormGroup,
  FormSelect,
  FormSelectOption,
  Modal,
  ModalVariant,
  TextInput
} from '@patternfly/react-core';
import {dispatchProps} from './ContextApi/FruitsProvider';
import {ADD_FRUIT_ACTION, FRUIT_SEASON_OPTIONS} from '../constants';

const AddFruits: React.FC<{ dispatch: dispatchProps }> = ({dispatch}) => {
  const [fruitName, setFruitName] = React.useState<string>('');
  const [fruitSeason, setFruitSeason] = React.useState<string>('');
  const [isModalOpen, setIsModalOpen] = React.useState<boolean>();
  const [error, setError] = React.useState<boolean>(false);
  const [errorMessage, setErrorMessage] = React.useState<string>('You must fill out all required fields before you can proceed.');

  const handleModalToggle = () => {
    setError(false);
    setFruitName('');
    setFruitSeason('');
    setIsModalOpen((modalopen) => !modalopen)
  }

  const handleModalSubmit = () => {
    console.log(fruitName, fruitSeason);
    const requestBody = {
      name: fruitName,
      season: fruitSeason
    }
    if (fruitName && fruitSeason) {
      const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(requestBody)
      };
      fetch(`${process.env.REACT_APP_API_URL}/fruit`, requestOptions)
      .then((resp) => {
        console.log(resp);
        dispatch({
          type: ADD_FRUIT_ACTION,
          payload: {
            cells: [
              {title: fruitName},
              {title: fruitSeason},
              {title: "delete", fruitname: fruitSeason}
            ]
          }
        });
        setError((err) => !err);
        setIsModalOpen((modalopen) => !modalopen);
        setFruitName('');
        setFruitSeason('');

      })
      .catch((err) => {
        console.log(err);
        setError((err) => !err);
        setErrorMessage(`${err.status} - ${err.errorText}`)
      })
    } else {
      setError((err) => !err);
    }
  }

  return (
      <>
        <Button variant="primary" onClick={handleModalToggle}>
          Add Fruits
        </Button>
        <Modal
            title="Add Fruits"
            variant={ModalVariant.medium}
            isOpen={isModalOpen}
            onClose={handleModalToggle}
            actions={[
              <Button key="confirm" variant="primary"
                      onClick={handleModalSubmit}>
                Confirm
              </Button>,
              <Button key="cancel" variant="link" onClick={handleModalToggle}>
                Cancel
              </Button>
            ]}
        >
          <Form>
            <FormGroup label="Name" isRequired fieldId="simple-form-fruit">
              <TextInput
                  isRequired
                  id="simple-form-fruit"
                  type="text"
                  placeholder="Enter Fruit Name"
                  name="simple-form-fruit"
                  value={fruitName}
                  aria-label="Fruit Name"
                  onChange={(val) => setFruitName(val)}
              />
            </FormGroup>
            <FormGroup label="Season" isRequired fieldId="simple-form-season">
              <FormSelect isRequired
                          id="simple-form-season"
                          placeholder="Enter Fruit Season"
                          name="simple-form-season"
                          value={fruitSeason}
                          aria-label="Fruit Season"
                          onChange={(val) => val !== "please choose" ? setFruitSeason(val) : setError((err) => !err)}>
                {FRUIT_SEASON_OPTIONS.map((option, index) => (
                    <FormSelectOption isDisabled={option.disabled} key={index}
                                      value={option.value}
                                      label={option.label}/>
                ))}
              </FormSelect>
            </FormGroup>
            {error && (
                <FormAlert>
                  <Alert variant="danger"
                         title={errorMessage}
                         aria-live="polite"
                         isInline/>
                </FormAlert>
            )}
          </Form>
        </Modal>
      </>
  )
}

export default AddFruits;
