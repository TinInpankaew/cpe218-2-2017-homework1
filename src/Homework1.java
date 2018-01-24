//package components;
import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Stack;

public class Homework1 extends JPanel
		implements TreeSelectionListener {
	private JEditorPane htmlPane;
	private JTree jTree;

	//Optionally play with line styles.  Possible values are
	//"Angled" (the default), "Horizontal", and "None".
	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";

	//Optionally set the look and feel.
	private static boolean useSystemLookAndFeel = false;
	public static Node root;
	public static String Screen;

	public Homework1() {
		super(new GridLayout(1,0));

		//Create the nodes.
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode(root);
		CreateUI(root,top);

		//Create a tree that allows one selection at a time.
		jTree = new JTree(top);
		jTree.getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		jTree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			jTree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(jTree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));
        // Icon
		ImageIcon leafIcon = createImageIcon("middle.gif");
		if (leafIcon != null) {
			DefaultTreeCellRenderer renderer =
					new DefaultTreeCellRenderer();
			renderer.setClosedIcon(leafIcon);
			renderer.setOpenIcon(leafIcon);
			jTree.setCellRenderer(renderer);
		}
                
		add(splitPane);
	}

	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Homework1.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.out.println("Couldn't find file!!!");
			return null;
		}
	}
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				jTree.getLastSelectedPathComponent();

		if (node == null) return;
        Object nodeInfo = node.getUserObject();
        Screen = GetSol((Node)nodeInfo);
        htmlPane.setText(Screen);
	}
        
    public static void CreateUI(Node n, DefaultMutableTreeNode top){
        if(n.Node_Right!=null)
        {
            DefaultMutableTreeNode Right=new DefaultMutableTreeNode(n.Node_Right);
            top.add(Right);
            CreateUI(n.Node_Right,Right);
        }
		if(n.Node_Left!=null)
		{
			DefaultMutableTreeNode left=new DefaultMutableTreeNode(n.Node_Left);
			top.add(left);
			CreateUI(n.Node_Left, left);
		}
        
    }


	private static void createAndShowGUI() {
		if (useSystemLookAndFeel) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("Couldn't use system look and feel.");
			}
		}

		//Create and set up the window.
		JFrame frame = new JFrame("Binary Tree Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new Homework1());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
            
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
            root = new Node('E');

            root.Value = args[0];
            //root.Value = "251-*32*+";
            root = CreateTree(root);
        System.out.println(GetSol(root));
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
            
    public static String GetSol(Node n)
    {
        String Ans = new String();
        String[] Answ = Infix(n).split("");
        for(int i=1; i<Infix(n).length()-1;i++)
        {
            Ans += Answ[i];
        }
        if(isNumber(n.Op))
        {
            return (n.Op+"");
        }else
            return (Ans + "=" +Calculate(n));
    }


    public static Node CreateTree(Node n)
    {
        Stack<Character> Stack_Question=new Stack<Character>();
        String[] Arr_Of_Question = n.Value.split("");
        
        for(String Arr_Q : Arr_Of_Question )
        {
            Stack_Question.add(Arr_Q.charAt(0));
        }
        if(Stack_Question.size()<=1){
            System.out.println("Wrong Input!!!");
            return null;
        }else
        n = toTree(n,Stack_Question);
        
        return n;
    }

    public static String Infix(Node n){
        
        if(isOperator(n.Op))
        {
            return ("(" +Infix(n.Node_Right)+ n.Op+ Infix(n.Node_Left) +")");
        }else 
        {
            return (""+ n.Op);
        }
             
    }
    
    public static String InOrder(Node n){
        if( n.Node_Left != null && n.Node_Right != null){
             return (n.Op +" " + InOrder(n.Node_Right) + " " + InOrder(n.Node_Left)) ;   
        }else return (n.Op+"");

    }

    public static int Calculate(Node n){
 //       System.out.println(n.Op);
        if(isOperator(n.Op)){
            if(n.Op=='+')
            {
                return Calculate(n.Node_Left)+Calculate(n.Node_Right);
            }else if(n.Op=='-')
            {
                return Calculate(n.Node_Right)-Calculate(n.Node_Left);
            }else if(n.Op=='*')
            {
                return Calculate(n.Node_Left)*Calculate(n.Node_Right);
            }else if(n.Op=='/')
            {
                return Calculate(n.Node_Right)/Calculate(n.Node_Left);
            }
        }else if(isNumber(n.Op)){
            return n.Op-'0';
        }else {
            System.out.println("Calculate Error!!");
        }
        
        return n.Ans;
    }


    public static class Node{
        String Value;
        Character Op;
        int Ans;
        Node Node_Left, Node_Right;
        public Node(Character Op){
            this.Op = Op;
        }
        public String toString() {
            return Op.toString();
        }
    }
    
    public static Node toTree(Node root, Stack<Character> Ques)
    {
        root = new Node(Ques.pop());
        if(isOperator(Ques.peek())){
     //       System.out.println(isOperator(Ques.peek()) +" OP Left "+ Ques.peek());
            if(Ques.size()<=1){
                System.out.println("Error Ran Out of Number L");
                return root;
            }else{
                root.Node_Left = toTree(root.Node_Left, Ques);
            }
        }else if(isNumber(Ques.peek()))
        {
    //        System.out.println(root.Op + " Number Left");
            root.Node_Left = new Node(Ques.pop());
        }else
        {
                System.out.println("Error Wrong Input L!!");
                return root;
        }
        if(isOperator(Ques.peek())){
   //         System.out.println(isOperator(Ques.peek()) +" OP Right "+ Ques.peek());
            if(Ques.capacity()<=1){
                System.out.println("Error Ran Out of Number R");
                return root;
            }else{
                root.Node_Right = toTree(root.Node_Right, Ques);
            }
        }else if(isNumber(Ques.peek()))
        {
   //         System.out.println(root.Op + " Number Right");
            root.Node_Right = new Node(Ques.pop());
        }else
        {
                System.out.println("Error Wrong Input!! R");
                return root;
        }

        return root;
    }
    
    
    public static boolean isNumber(char s) {  
        
       if ("1234567890".indexOf(s) != -1){
           return true;
       }
       return false;
    }  
    
    public static boolean isOperator(char s){
        if("+-*/".indexOf(s)!=-1)
        {
            return true;
        }
        return false;
    }   
}




