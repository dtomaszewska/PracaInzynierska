import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeListener;

public class OptionsWindow{
  private static int[] response = {1,1,1};//{"4 fields", "Circle", "beginner"};
  private Settings set;
  public OptionsWindow(Settings my_settings) 
  {
	set = my_settings;
  }

  public void main(String[] args) {
    final JFrame frame = new JFrame("Settings");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final Container fieldsContainer = RadioButtonUtils.createRadioButtonGrouping(
        set.fieldsOptions, "Fields Count", new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	if(e.getSource() instanceof JRadioButton){
                    JRadioButton radioButton = (JRadioButton) e.getSource();
                    if(radioButton.isSelected()){
                    	response[0]=RadioButtonUtils.getNumberOfElement(radioButton.getText(), set.fieldsOptions)+1;
                    	System.out.println(response[0]);
                    }
                }        	
            }
        }, null, null);
    final Container iconContainer = RadioButtonUtils.createRadioButtonGrouping(
            set.iconOptions, "Circle or Cross?", new ActionListener() {
            	public void actionPerformed(ActionEvent e)
                {
                	if(e.getSource() instanceof JRadioButton){
                        JRadioButton radioButton = (JRadioButton) e.getSource();
                        if(radioButton.isSelected()){
                        	response[1]=RadioButtonUtils.getNumberOfElement(radioButton.getText(), set.iconOptions)+1;
                        	System.out.println(response[1]);
                        }
                    }        	
                }
            }, null, null);
    final Container difficultyContainer = RadioButtonUtils.createRadioButtonGrouping(
        set.difficultyOptions, "Difficulty",  new ActionListener() {
        	public void actionPerformed(ActionEvent e)
            {
            	if(e.getSource() instanceof JRadioButton){
                    JRadioButton radioButton = (JRadioButton) e.getSource();
                    if(radioButton.isSelected()){
                    	response[2]=RadioButtonUtils.getNumberOfElement(radioButton.getText(), set.difficultyOptions)+1;
                    	System.out.println(response[2]);
                    }
                }        	
            }
        }, null, null);
    Container contentPane = frame.getContentPane();
    contentPane.add(fieldsContainer, BorderLayout.WEST);
    contentPane.add(iconContainer);
    contentPane.add(difficultyContainer, BorderLayout.EAST);
    
    JButton OK = new JButton("OK");
    
    OK.addActionListener(new ActionListener() {
    	 
        public void actionPerformed(ActionEvent e)
        {
        	set.options = response;
        	set.exit = true;
        	frame.setVisible(false);
        }
    });      
	frame.add(OK, BorderLayout.SOUTH);
    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}

class RadioButtonUtils {
  private RadioButtonUtils() {}
  
  public static int getNumberOfElement(String field, String[] container){
	  int number = 0;
	    for (int i = 0, n = container.length; i < n; i++) {
	      if (field == container[i]) {
	          number = i;
	      }
	    }
	    return number;
  }

   public static Container createRadioButtonGrouping(String elements[],
      String title, ActionListener actionListener,
      ItemListener itemListener, ChangeListener changeListener) {
    JPanel panel = new JPanel(new GridLayout(0, 1));
    //   If title set, create titled border
    if (title != null) {
      Border border = BorderFactory.createTitledBorder(title);
      panel.setBorder(border);
    }
    //   Create group
    ButtonGroup group = new ButtonGroup();
    JRadioButton aRadioButton;
    //   For each String passed in:
    //   Create button, add to panel, and add to group
    for (int i = 0, n = elements.length; i < n; i++) {
      aRadioButton = new JRadioButton(elements[i]);
      panel.add(aRadioButton);
      group.add(aRadioButton);
      if (actionListener != null) {
        aRadioButton.addActionListener(actionListener);
      }
      if (itemListener != null) {
        aRadioButton.addItemListener(itemListener);
      }
      if (changeListener != null) {
        aRadioButton.addChangeListener(changeListener);
      }
    }
    return panel;
  }
}
