
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class memAlloc {

    static Random random = new Random();
    static LinkedList<memBlock> memoryBlockLists = new LinkedList<>();
    static piriorityQueue startingList = new piriorityQueue();
    static piriorityQueue waitingList = new piriorityQueue();
    static LinkedList<process> runingList = new LinkedList<>();
    static memBlock block = new memBlock(1024, 0);

    public static void main(String[] args) throws Exception {

        memoryBlockLists.add(block);
        insertingProcesses(10);
        long timeCount = 1;

        while (!startingList.isEmpty() || !waitingList.isEmpty() || !runingList.isEmpty()) {
            allocationProcesses();
            Thread.sleep(1000);
            System.out.println("At Time " + timeCount);
            System.out.println("Runing: ");
            showRuningList();
            System.out.println("-------------------");

            System.out.println("Waiting: ");
            showWaitingList();
            System.out.println("-------------------");
            processCompletion();
            timeCount++;

        }

    }

    public static void insertingProcesses(int numberOfProcess) {
        for (int i = 1; i <= numberOfProcess; i++) {
            long size = random.nextInt(256) + 1;
            long time = random.nextInt(7000) + 1;               // changed, lessen time bound for shorter output

            process Process = new process(size, time);
            System.out.println(Process);
            startingList.insert(Process);

        }

    }

    public static void processCompletion() {
        Iterator<process> iterator = runingList.iterator();
        while (iterator.hasNext()) {
            process Process = iterator.next();
            Process.setTimeOut(Process.getTimeOut() - 1000);

            if (Process.getTimeOut() <= 0) {
                memoryBlockLists.add(new memBlock(Process.getSize(), Process.getMemBlocks().getStartAddress()));

                iterator.remove();
            }

        }
        mergeMemoryBlocks();

    }

    public static void mergeMemoryBlocks() {
        
        memoryBlockLists.sort(Comparator.comparingLong(block -> block.getStartAddress()));

        LinkedList<memBlock> mergedBlocks = new LinkedList<>();
        memBlock prev = memoryBlockLists.get(0);

        for (int i = 1; i < memoryBlockLists.size(); i++) {
            memBlock current = memoryBlockLists.get(i);
            if (prev.getEndAddress() + 1 == current.getStartAddress()) {

                prev = new memBlock(prev.getStartAddress(), prev.getSize() + current.getSize());
            } else {
                mergedBlocks.add(prev);
                prev = current;
            }
        }
        mergedBlocks.add(prev);

        memoryBlockLists = new LinkedList<>(mergedBlocks);
    }

    public static void allocationProcesses() {

        if (!startingList.isEmpty()&&waitingList.isEmpty()) {
            process Process = startingList.dequeue();
            for (memBlock elem : memoryBlockLists) {
                if (Process.getSize() <= elem.getSize()) {

                    Process.memBlocks = new memBlock(Process.getSize(), elem.getStartAddress());

                    runingList.add(Process);
                    block.setStartAddress(Process.getMemBlocks().getEndAddress() + 1);
                    block.setSize(block.getSize() - Process.getMemBlocks().getSize());

                    return;

                }

            }
            waitingList.insert(Process);

        } else if (!waitingList.isEmpty()) {
            process Process = waitingList.dequeue();
            for (memBlock elem : memoryBlockLists) {
                if (Process.getSize() <= elem.getSize()) {

                    Process.memBlocks = new memBlock(Process.getSize(), elem.getStartAddress());

                    runingList.add(Process);
                    block.setStartAddress(Process.getMemBlocks().getEndAddress() + 1);
                    block.setSize(block.getSize() - Process.getMemBlocks().getSize());

                    return;

                }

            }

        }

    }

    public static void showWaitingList() {
        waitingList.printQueue();

    }

    public static void showRuningList() {
        for (process elem : runingList) {

            System.out.println(elem);

        }
    }

}
