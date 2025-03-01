package com.algosenpai.app.logic.chapters.chapter2;

import com.algosenpai.app.logic.chapters.Question;
import com.algosenpai.app.logic.models.ReviewTracingListModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class QueuePopPushQuestion extends Question {

    private static int queueSize;

    QueuePopPushQuestion() {
        //Generates a size for the queue between 4 and 8.
        queueSize = getRandomNumber(7,4);
        //Populates the queue with positive integers under 100.
        LinkedList<Integer> queue = createList(queueSize);
        ArrayList<String> instructions = new ArrayList<>();
        //Determines the number of instructions to be added.
        int numberOfInstructions = getRandomNumber(3, 3);
        //Populate instructions
        addInstructions(instructions, numberOfInstructions);

        questionFormatter();
        question += printQueue(queue);
        question += printInstructions(instructions);
        //Updates the queue according to the question.
        changeQueue(instructions, queue);
        answer = String.valueOf(queue.getLast());
    }

    @Override
    public void questionFormatter() {
        question = "A Queue of size " + queueSize
                + " undergoes a series of operations as shown below.\n"
                + "What would be the new value called upon queue.peek()?";
    }

    /**
     * Creates a Linked List and populate it with unique numbers.
     *
     * @param size The number of elements to be in the Linked List.
     * @return The Linked List data structure to be used for the question.
     */
    private static LinkedList<Integer> createList(int size) {
        HashSet<Integer> set = new HashSet<>();
        while (set.size() != size) {
            int value = getRandomNumber(0, 100);
            set.add(value);
        }
        return new LinkedList<>(set);
    }

    /**
     * Adds new instructions to the list through random generation of either 1 or 0.
     * If values is 0, a push command is added along with a value. Else, a pop
     * command is added.
     *
     * @param instructions         The list where instructions will be added.
     * @param numberOfInstructions The number of instructions to be added into the
     *                             list.
     */
    private static void addInstructions(ArrayList<String> instructions, int numberOfInstructions) {
        for (int i = 0; i < numberOfInstructions; i++) {
            int val = getRandomNumber(0,2);
            int toadd = getRandomNumber(0,100);
            switch (val) {
            case 0:
                String combined = "Push(" + toadd + ");";
                instructions.add(combined);
                break;
            case 1:
                instructions.add("Pop();");
                break;
            default:
                break;
            }
        }
    }

    /**
     * Creates a formatted String which contains the elements in the queue.
     *
     * @param queue The queue containing the elements.
     * @return The formatted String.
     */
    private static String printQueue(LinkedList<Integer> queue) {
        StringBuilder q = new StringBuilder();
        for (int i : queue) {
            q.append("[").append(i).append("] -> ");
        }
        q.append("Front\n");
        return q.toString();
    }

    /**
     * Creates a String with the instruction given by the list on separate new lines.
     *
     * @param instructions The list of instructions provided.
     * @return The String containing the instructions given by the list.
     */
    private static String printInstructions(ArrayList<String> instructions) {
        StringBuilder instructs = new StringBuilder();
        int i = 1;
        for (String s : instructions) {
            instructs.append(i).append(". ").append(s).append("\n");
            i++;
        }
        return instructs.toString();
    }

    /**
     * Changes the queue according to the instructions given. If instruction is pop,
     * the front value of the list would be removed, or else a new value will be
     * pushed into the queue.
     *
     * @param instructions The list of instructions given.
     * @param queue        The list which would be edited according to the
     *                     instructions given.
     */
    private static void changeQueue(ArrayList<String> instructions, LinkedList<Integer> queue) {
        rtlm = new ReviewTracingListModel();
        rtlm.addReviewStep("This is the current queue.");
        rtlm.addReviewStep(printQueue(queue));
        for (String cmd : instructions) {
            rtlm.addReviewStep("Consider step : " + cmd);
            if (cmd.contains("Pop")) {
                rtlm.addReviewStep("Removing this element : " + queue.pollLast() + ".");
            } else {
                String number = cmd.substring(5, cmd.length() - 2);
                rtlm.addReviewStep("Adding this number to the back : " + number + ".");
                int valuetoadd = Integer.parseInt(number);
                queue.addFirst(valuetoadd);
            }
            rtlm.addReviewStep("This is the new queue.");
            rtlm.addReviewStep(printQueue(queue));
        }
        rtlm.addReviewStep("This is the final queue.");
        rtlm.addReviewStep(printQueue(queue));
    }

}
