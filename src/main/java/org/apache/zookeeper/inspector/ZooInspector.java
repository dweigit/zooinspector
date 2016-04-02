/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zookeeper.inspector;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.zookeeper.inspector.gui.ZooInspectorPanel;
import org.apache.zookeeper.inspector.logger.LoggerFactory;
import org.apache.zookeeper.inspector.manager.ZooInspectorManagerImpl;

/**
 *
 */
public class ZooInspector {

    public static void main(String[] args) {

        try {
            initGlobalFont(new Font("Microsoft YaHei", Font.PLAIN, 12));  //统一设置字体
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame frame = new JFrame("ZooInspector");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final ZooInspectorPanel zooInspectorPanel = new ZooInspectorPanel(new ZooInspectorManagerImpl());
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    zooInspectorPanel.disconnect(true);
                }
            });
            frame.setContentPane(zooInspectorPanel);

            Dimension screenSize = getScreenResolution();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
            frame.setSize(screenWidth * 3 / 4, screenHeight * 3 / 4);
            frame.setLocationRelativeTo(null);//设置窗口居中显示
            frame.setVisible(true);
        } catch (Exception e) {
            LoggerFactory.getLogger().error("Error occurred loading ZooInspector", e);
            JOptionPane.showMessageDialog(null, "ZooInspector failed to start: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Dimension getScreenResolution() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = ge.getScreenDevices();
        for (int i = 0; i < screenDevices.length; i++) {
            DisplayMode dm = screenDevices[i].getDisplayMode();
            int screenWidth = dm.getWidth();
            int screenHeight = dm.getHeight();
            return new Dimension(screenWidth, screenHeight);
        }
        return null;
    }

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    private static void initGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys();
             keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}
