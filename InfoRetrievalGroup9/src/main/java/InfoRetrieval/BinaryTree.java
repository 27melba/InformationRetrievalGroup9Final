package InfoRetrieval;


public class BinaryTree {
    static class Node {
        double value;
        int index;
        Node left, right;

        Node(double value,int index){
            this.value = value;
            this.index = index;
            left = null;
            right = null;
        }
    }

    public void insert(Node node, double value,int index) {
        if (value < node.value)
        {
            if (node.left != null)
            {
                insert(node.left, value,index);
            }
            else
            {
//        		System.out.println(" Inserted " + value + " to left of " + node.value);
                node.left = new Node(value,index);
            }
        }
        else if (value > node.value) {
            if (node.right != null) {
                insert(node.right, value,index);
            } else {
//            System.out.println("  Inserted " + value + " to right of "
//                + node.value);
                node.right = new Node(value,index);
            }
        }
    }
    public void traverseInOrder(Node node,int i) {
        if (node != null && i>=0) {
            --i;
            traverseInOrder(node.right,i);
            System.out.println(node.index + " " + node.value);
            --i;

            traverseInOrder(node.left,i);
        }
    }

}