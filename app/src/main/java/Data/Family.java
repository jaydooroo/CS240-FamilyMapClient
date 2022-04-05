package Data;

import model.Person;

public class Family {

    String relationship;
    Person person;

    public Family () {

    }

    public Family (String relationship, Person person) {
        this.person = person;
        this.relationship = relationship;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }



}
