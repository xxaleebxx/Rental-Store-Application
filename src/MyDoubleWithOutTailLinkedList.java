package W22Project3MyDoulbleWIthOutTailLinklistGIVETOSTUDENTS;

import java.io.Serializable;
import java.util.Random;

/*****************************************************************
 * MyDoubleWithOutTailLinkedList class contains the logic for
 * adding, removing, and displaying rental objects
 * with helper methods
 *
 *
 * @author Autumn Bertram
 * @version Winter 2022
 *
 *****************************************************************/



public class MyDoubleWithOutTailLinkedList implements Serializable {

    private DNode top;

    public MyDoubleWithOutTailLinkedList() {
        top = null;
    }

    // This method has been provided, and you are not permitted to modify
    /**
     * Counts the number of nodes in the list and returns valid total
     *
     * @return int size of the list
     * @throws RuntimeException when number of links forward does not match the number of links going backward
     * */
    public int size() {
        if (top == null)
            return 0;

        int total = 0;
        DNode temp = top;
        while (temp != null) {
            total++;
            temp = temp.getNext();
        }

        int totalBack = 0;
        temp = top;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }

        while (temp != null) {
            totalBack++;
            temp = temp.getPrev();
        }

        if (total != totalBack) {
            System.out.println("next links " + total + " do not match prev links " + totalBack);
            throw new RuntimeException();
        }

        return total;

    }

    // This method has been provided and you are not permitted to modify
    /**
     * Empties the list
     * @return none
     * */
    public void clear() {
        Random rand = new Random(13);
        while (size() > 0) {
            int number = rand.nextInt(size());
            remove(number);
        }
    }

//--------------------------------------------------ADD HELPER METHODS-------------------------------------------------
    /**
     * Adds the node to the top of the list
     * @param node the node to be added
     * */
    private void addAtTop(DNode node){

        if (top == null) {
            top = node;
        } else {
            DNode temp = top;
            top = node;
            top.setNext(temp);
            top.setPrev(null);
            temp.setPrev(top);
            System.out.println();
        }
    }

    /**
     * Inserts the current node before the checked node
     * @param check DNode the node we add current before
     * @param current DNode the node to be added
     * */
    private void insertBefore(DNode current, DNode check){
        if (check == top) {
            addAtTop(current);
        }
        else{
            current.setNext(check);
            current.setPrev(check.getPrev());
            check.getPrev().setNext(current);
            check.setPrev(current);
        }
    }

    /**
     * Inserts the current node after the checked node
     * @param check DNode the node we add current after
     * @param current DNode the node to be added
     * */
    private void insertAfter(DNode current, DNode check){
        current.setNext(check.getNext());
        if(check.getNext() != null) check.getNext().setPrev(current);
        current.setPrev(check);
        check.setNext(current);
    }

//----------------------------------------------------END OF ADD HELPER METHODS----------------------------------------

    /**
     * Add object to the collection, sorted by Games first (ordered by dueDate)
     * and by Consoles second (ordered by dueDate)
     * if same dueDate, order by renter's name
     *
     * @param s Rental the object to be added
     * */
    public void add(Rental s) {

        DNode current = new DNode(s, null, null);//node to sort

        // if no list
        if (top == null) {
            addAtTop(current);
            return;
        }

        DNode check = top; //node to compare to

        if (check == null){
            throw new NullPointerException("Top is null. addAtTop() did not work");
        }

        if (s instanceof Game){

            if (check.getData() instanceof Game){ // if same instanceOf, check due date                                                                 //s: game, check: game
                while (check != null && check.getNext() != null &&
                        (s.dueBack.after(check.getData().dueBack))
                        && (check.getNext().getData() instanceof Game)){ //while the value after check is due before S and a game, set check to next node //sorts thru values of same type

                    check = check.getNext();
                } //breaks when reaches end of list, value after check is due after s, or value after check is a console

                if (check.getData().getDueBack().equals(s.getDueBack())){

                    while (check.getNext() != null &&check.getNext().getData().dueBack.equals(s.dueBack) &&
                            s.nameOfRenter.compareTo(check.getData().nameOfRenter) > 0 ){ //while the name after check comes before s, set check to next value //sorts thru the values of the same date
                        check = check.getNext();
                    }
                    if (s.nameOfRenter.compareTo(check.getData().nameOfRenter) > 0 ) insertAfter(current,check);
                    else if (s.nameOfRenter.compareTo(check.getData().nameOfRenter) < 0 ) insertBefore(current, check);
                    else insertBefore(current, check);
                }
                else if (s.dueBack.after(check.getData().dueBack)) insertAfter(current, check); // current is due after
                else insertBefore(current, check);
            }

            else{ // s is game, check is console --> s goes before check                                                                                //s: game, check: console
                insertBefore(current, check);
            }
        }
        else {// s is a console

            while (check.getNext() != null && check.getData() instanceof Game)
                check = check.getNext(); //sets check to the first console or last value (game)

            if (check.getData() instanceof Game) {//means there are no consoles                                                                     //s: console, check: game
                insertAfter(current, check);
            }
            else {                                                                                                                                 //s:console, check: console
                while (check != null && check.getNext() != null && (s.dueBack.after(check.getData().dueBack))) { //while the value after check is due before S, set check to next node
                    check = check.getNext();
                }

                if (check.getData().getDueBack().equals(s.getDueBack())){

                    while (check.getNext() != null &&check.getNext().getData().dueBack.equals(s.dueBack) &&
                            s.nameOfRenter.compareTo(check.getData().nameOfRenter) > 0 ){ //while the name after check comes before s, set check to next value //sorts thru the values of the same date
                        check = check.getNext();
                    }
                    if (s.nameOfRenter.compareTo(check.getData().nameOfRenter) > 0 ) insertAfter(current,check);
                    else if (s.nameOfRenter.compareTo(check.getData().nameOfRenter) < 0 ) insertBefore(current, check);
                    else insertBefore(current, check);
                }
                else if (s.dueBack.after(check.getData().dueBack)) insertAfter(current, check); // current is due after
                else insertBefore(current, check);
            }
        }
    }




    /**
     * Removes node at the specified index
     *
     * @param index the index of the node to be removed
     * @return Rental the data of the node removed
     * @throws IllegalArgumentException if invalid index
     * */
    public Rental remove(int index) {
        if (index < 0 || index >= size())
            throw new IllegalArgumentException();

        if (top != null) {

            DNode prev = top;
            Rental found;

            if (index == 0 && size()==1) {
                found = top.getData();
                top = null;
            }
            else if (index == 0){
                found = top.getData();
                top.getNext().setPrev(null);
                top.setNext(null);

            }
            else {
                int count = 0;
                while (count != index) {
                    prev= prev.getNext();
                    count++;
                }
                found = prev.getData();
                prev.getPrev().setNext(prev.getNext());
                if (prev.getNext() != null) prev.getNext().setPrev(prev.getPrev());

            }
           return found;
        }

        return null;
    }

    /**
     * Gets the Rental at the specified index
     * @param index the index to be found
     * @return Rental the object found
     * */
    public Rental get(int index) {

        if (top == null)
            return null;

        DNode current = top;
        int count = 0;
        while (count < index) {
            current = current.getNext();
            count++;
        }

        return current.getData();
    }

    /**
     * Prints the data of each node in the list
     * @return none
     * */
    public void display() {
        DNode temp = top;
        while (temp != null) {
            System.out.println(temp.getData());
            temp = temp.getNext();
        }
    }

    /**
     * Converts object to string in format
     * LL {top=  , size=  }
     *
     * @return String string format
     * */
    public String toString() {
        return "LL {" +
                "top=" + top +
                ", size=" + size() +
                '}';
    }

}