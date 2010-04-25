package akerigan.fb2.gui.jgoodies;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.*;

public class BindingTest {

    public static void main(String[] args) {

        MyBean bean = new MyBean();

        // Bean adapter is an adapter that can create many value model objects for a single
        // bean. It is more efficient than the property adapter. The 'true' once again means
        // we want it to observe our bean for changes.
        BeanAdapter adapter = new BeanAdapter(bean, true);

        ValueModel booleanModel = adapter.getValueModel("booleanValue");
        ValueModel stringModel = adapter.getValueModel("stringValue");
        // creates a JCheckBox with the property adapter providing the underlying model.
        JCheckBox box = BasicComponentFactory.createCheckBox(booleanModel, "Boolean Value");
        JTextField field = BasicComponentFactory.createTextField(stringModel);
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(2, 1));
        frame.getContentPane().add(box);
        frame.getContentPane().add(field);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
