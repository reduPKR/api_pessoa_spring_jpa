package com.digitalinnovation.personaapi.service;

import com.digitalinnovation.personaapi.dto.entity.PersonDTO;
import com.digitalinnovation.personaapi.dto.response.MessageResponseDTO;
import com.digitalinnovation.personaapi.entity.Person;
import com.digitalinnovation.personaapi.exception.PersonNotFoundException;
import com.digitalinnovation.personaapi.mapper.PersonMapper;
import com.digitalinnovation.personaapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(PersonDTO personDTO){
        Person person = personMapper.toModel(personDTO);
        Person new_person = personRepository.save(person);
        return messageReturn("Usuario cadastrado seu id é: "+new_person.getId());
    }

    private MessageResponseDTO messageReturn(String message) {
        return MessageResponseDTO.builder()
                .message(message)
                .build();
    }

    public List<PersonDTO> getAll() {
        List<Person> people = personRepository.findAll();
        return people.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO getById(long id) throws PersonNotFoundException {
        Person person = verifyExists(id);

//        if(optional.isPresent())
//            throw new PersonNotFoundException(id);

        return personMapper.toDTO(person);
    }

    private Person verifyExists(long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public void delete(long id) throws PersonNotFoundException{
        verifyExists(id);
        personRepository.deleteById(id);
    }

    public MessageResponseDTO update(long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyExists(id);
        Person person = personMapper.toModel(personDTO);
        Person new_person = personRepository.save(person);
        return messageReturn("Usuario com id: "+new_person.getId()+" Foi atualizado");
    }
}
