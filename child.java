import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;


public class child extends character_unit
{
    //A child. Assigned a mother, can be assigned a father by the user. A child inferits skills, stats, and classes from 
    //their two parents.
    //The object lucina in main_code is the only exception, as she is assigned a father and her mother can be assigned
    //by the user.
    
    
    static ArrayList<child> all_children=new ArrayList<child>();
    
    child_review_panel review_panel;
    
    //variables related to parents
    parent_unit constant_parent;
    parent_unit flexable_parent;
    Char_Attached_JButton clear_parent_button;
    JLabel flexable_parent_label=new JLabel("<html>Parent No.<br>1: ---<br>2: None</html>", SwingConstants.CENTER);
    
    
    //Initiates child()
    //Takes as peramaters: a String refering to what this child will be called, a boolean that is true if this child is
    //female, and a parent_unit for whomever this child's constant parent is.
    public child(String name, boolean is_female, parent_unit constant_parent)
    {
        super(name, is_female, false);
        
        //Adjusts this child's constant parent object
        this.constant_parent=constant_parent;
        constant_parent.parent_of=this;
        ((parent_info_frame)constant_parent.info_frame).child1_label.setText("Constant Child: "+this.name);
        
        //Builds UI
        info_button=new Char_Attached_JButton(this, name);
        info_button.addActionListener(new InfoButtonListener());
        
        on_team_info_button=new Char_Attached_JButton(this, name);
        on_team_info_button.addActionListener(new InfoButtonListener());
        
        clear_parent_button=new Char_Attached_JButton(this, "Clear Parent");
        clear_parent_button.addActionListener(new ClearParentListener());
        
        buttons_panel.add(clear_parent_button, BorderLayout.NORTH);
        buttons_panel.add(info_button, BorderLayout.CENTER);
        buttons_panel.add(flexable_parent_label, BorderLayout.SOUTH);
        
        info_frame=new child_info_frame(this, true);
        
        all_children.add(this);
    }
    
    
    //If a secondary parrent is added
    //Takes as a peramater who the new secondary parent is
    public void add_parent2(parent_unit parent2){
        flexable_parent=parent2;
        ((parent_info_frame)parent2.info_frame).child2_label.setText("Flexable Child: "+name);
        ((child_info_frame)info_frame).reset_classes();
        
        constant_parent.info_frame.married_to_label.setText("Married to: "+flexable_parent.name);
        flexable_parent.info_frame.married_to_label.setText("Married to: "+constant_parent.name);
        flexable_parent_label.setText("<html>Parent No.<br>1: "+constant_parent.name+"<br>2: "+flexable_parent.name+"</html>");
        
        info_frame.pack();
        
    }
    
    
    //If a secondary parrent is removed
    public void remove_parent2(){
        
        if (flexable_parent!=null){
            ((parent_info_frame)flexable_parent.info_frame).child2_label.setText("");
            flexable_parent=null;
        }
        
        ((child_info_frame)info_frame).reset_classes();
        
        main_code.units_on_team.remove(this);
        finished_frame.add_to_team_button.setText("Add to Team");
        main_code.update_team();
        
        finished_frame.setVisible(false);
        finished=false;
        
        info_frame.pack();
        
    }
    
    public void clear_unit(){
        remove_parent2();
        info_frame.setVisible(false);
    }
    
    
    //The listener for the button that clears a parent compleatly
    private class ClearParentListener implements ActionListener{
        
        public void actionPerformed(ActionEvent event){
            
            Char_Attached_JButton source=(Char_Attached_JButton)event.getSource();
            if (source.attached_to_child.flexable_parent!=null){
                if (source.attached_to_child.flexable_parent.parent_of!=null){
                    ((parent_info_frame)source.attached_to_child.constant_parent.info_frame).child2_label.setText("");
                    source.attached_to_child.flexable_parent.parent_of.flexable_parent=null;
                    source.attached_to_child.flexable_parent.parent_of.remove_parent2();
                }
                
                source.attached_to_child.flexable_parent.info_frame.married_to_label.setText("Married to: None");
                ((parent_info_frame)source.attached_to_child.flexable_parent.info_frame).child2_label.setText("");
                source.attached_to_child.flexable_parent.matched_with=null;
                source.attached_to_child.flexable_parent=null;
            }
            
            source.attached_to_child.constant_parent.info_frame.married_to_label.setText("Married to: None");
            source.attached_to_child.constant_parent.matched_with=null;
            source.attached_to_child.remove_parent2();
        }
    }
}