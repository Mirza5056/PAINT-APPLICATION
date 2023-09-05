/* ****************** Made By Kamran Akthar ************************************* */
/* ****************** simple Paint application in java through GUI *************** */
import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.text.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.*;
class MyDrawingPad extends Canvas   
{
    private int lastX,lastY,startX,startY,endX,endY;
    //private Cursor pencilCursor=new Cursor(Cursor.CROSSHAIR_CURSOR);
    private Cursor circleCursor=new Cursor(Cursor.CROSSHAIR_CURSOR);
    private Cursor rectangleCursor=new Cursor(Cursor.CROSSHAIR_CURSOR);
    private List<Point> points=new ArrayList<>();
    private boolean isEraserActive=false;
    private Cursor pencilCursor;
    private Cursor eraserCursor;
    private Color firstColor=Color.black;
    public boolean drawCircle=false;
    public boolean drawRectangle=false;
    private int circleStartX,circleStartY;
    private int rectangleStartX,rectangleStartY;
    private List<Shape> shapes=new ArrayList<>();
    private List<Rectangle> rectangles=new ArrayList<>();
    private List<Circle> circles=new ArrayList<>();
    private List<DrawLine> lines=new ArrayList<>();    
    private Shape currentShape;   
    /* ****************** Made By Kamran Akthar ************************************* */
    MyDrawingPad()
    {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        Image pencilImage = Toolkit.getDefaultToolkit().getImage("pencil2.png");
        pencilCursor = Toolkit.getDefaultToolkit().createCustomCursor(pencilImage, new Point(0, 0), "pencilCursor");
        Image eraserImage = Toolkit.getDefaultToolkit().getImage("eraser2.png");
        eraserCursor = Toolkit.getDefaultToolkit().createCustomCursor(eraserImage, new Point(0, 0), "eraserCursor");
        setCursor(pencilCursor);
        addMouseListener(new MouseAdapter() {
    public void mousePressed(MouseEvent e) 
    {
        if (drawCircle) 
        {
            circleStartX = e.getX();
            circleStartY = e.getY();
        }
        else if(drawRectangle)
        {
            rectangleStartX=e.getX();
            rectangleStartY=e.getY();
        }
        else 
        {
            lastX=e.getX();
            lastY=e.getY();
            points.add(new Point(lastX,lastY));
        }
        //return true;    
    }
    /* ****************** Made By Kamran Akthar ************************************* */
        public void mouseReleased(MouseEvent e) 
        {
            Graphics g=getGraphics();
            if (drawCircle) 
            {
                int radius = (int) Math.sqrt((e.getX() - circleStartX) * (e.getX() - circleStartX) +
                                            (e.getY() - circleStartY) * (e.getY() - circleStartY));
                currentShape = new Circle(circleStartX, circleStartY, radius);
                shapes.add(currentShape);
                repaint();
            }
            else if (drawRectangle) 
            {
                int width = e.getX() - rectangleStartX;
                int height = e.getY() - rectangleStartY;
                currentShape = new Rectangle(rectangleStartX, rectangleStartY, width, height);
                shapes.add(currentShape);
                repaint();
            }
            else
            {
                int width = e.getX();
                int height = e.getY();
                currentShape = new DrawLine(new ArrayList<>(points));
                shapes.add(currentShape);
                points.clear();
            }
        }
    });
    addMouseMotionListener(new MouseAdapter() {
    public void mouseDragged(MouseEvent e) 
    {
        Graphics g = getGraphics();
        //repaint();
        if (drawCircle) 
        {
            g.setColor(getForeground());
            int radius = (int) Math.sqrt((e.getX() - circleStartX) * (e.getX() - circleStartX) +
            (e.getY() - circleStartY) * (e.getY() - circleStartY));
            g.drawOval(circleStartX - radius, circleStartY - radius, radius * 2, radius * 2);
            repaint(); // Repaint the canvas to clear previous drawings
        }
        else if(drawRectangle)
        {
            //Graphics g=getGraphics();
            int width = e.getX() - rectangleStartX;
            int height = e.getY() - rectangleStartY;
            g.drawRect(rectangleStartX, rectangleStartY, width, height);
            g.setColor(isEraserActive ? getBackground() : firstColor);
            repaint(); // Repaint the canvas to clear previous drawings
        }
        /* ****************** Made By Kamran Akthar ************************************* */
        else
        {
            if (isEraserActive) 
            {
                //Graphics g = getGraphics();
                g.setColor(getBackground());
            } 
            else 
            {
                //Graphics g = getGraphics();
                g.setColor(isEraserActive ? getBackground() : firstColor);
            }
            g.drawLine(lastX, lastY, e.getX(), e.getY());
            points.add(new Point(e.getX(),e.getY()));
            lastX = e.getX();
            lastY = e.getY();
        }
    }
    });
    } 
    // Modify the DrawLine constructor to accept the correct arguments
    public void paint(Graphics g) 
    {
        super.paint(g);
        for (Shape shape : shapes) 
        {
            shape.draw(g);
        }
        for(Rectangle rectangle: rectangles)
        {
            rectangle.draw(g);
        }
        for(Circle circle: circles)
        {
            circle.draw(g);
        }
    }
/* ****************** Made By Kamran Akthar ************************************* */
    public interface Shape
    {
        void draw(Graphics g);
    }

    private class DrawLine implements Shape
    {
        private List<Point> points;    
        public DrawLine(List<Point> points) 
        {
            this.points = points;
        }
        @Override
        public void draw(Graphics g) 
        {
            g.setColor(getForeground());    
            if (points.size() >= 2) 
            {
                Point prevPoint = points.get(0);
                for (int i = 1; i < points.size(); i++) 
                {
                    Point currentPoint = points.get(i);
                    g.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                    prevPoint = currentPoint;
                }
            }
        }
    }

    private class Circle implements Shape
    {
        private int centerX, centerY, radius;
        public Circle(int centerX, int centerY, int radius) 
        {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
        @Override
        public void draw(Graphics g) 
        {
            g.setColor(getForeground());
            g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }
    }
    private class Rectangle implements Shape
    {
        private int x, y, width, height;
        public Rectangle(int x, int y, int width, int height) 
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        } 
        @Override
        public void draw(Graphics g) 
        {
            g.setColor(getForeground());
            g.drawRect(x, y, width, height);
        }
    }
    public List<Shape> getShapes()
    {
        return shapes;
    }

/* ****************** Made By Kamran Akthar ************************************* */

    public List<Point> getPoints()
    {
        return points;
    }
    public List<Shape> getCircle()
    {
        return shapes;
    }
    public void setFirstColor(Color color)
    {
        firstColor=color;
    }
    public Color getFirstColor()
    {
        return firstColor;
    }  
    public void setDrawRectangle(boolean drawRectangle)
    {
        this.drawRectangle=drawRectangle;
        setCursor(rectangleCursor);
        drawRectangle=true;
        drawCircle=false;
    }
    public void setDrawCircle(boolean drawCircle)
    {
        this.drawCircle=drawCircle;
        setCursor(circleCursor);
    }
    public void setPencilCursor()
    {
        setCursor(pencilCursor);
        isEraserActive=false;
    }
    public void setEraser()
    {
        setCursor(eraserCursor);
        isEraserActive=true;
    }
    public void resetCircle()
    {
        setCursor(pencilCursor);
        drawCircle=false;
    }
    public void resetRectangle()
    {
        setCursor(pencilCursor);
        drawRectangle=false;
    }
}
class CutPage extends JFrame
{
    public CutPage()
    {
        setBackground(Color.WHITE);
    }
}
/* ****************** Made By Kamran Akthar ************************************* */
class Drawing extends JFrame
{
    private MyDrawingPad pd;
    private JButton button;
    private Container container;
    private Color currentColor;
    private JScrollPane jsp;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Timer t;
    private int startX,startY,endX,endY;
    private boolean drawing;
    Drawing()
    {
        ImageIcon icon=new ImageIcon("paint.png");
        Image image=icon.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        setIconImage(image);
        JPanel timerPanel=new JPanel();
        JLabel timerLabel=new JLabel("               ");
        timerPanel.setPreferredSize(new Dimension(30,30));
        // /timerPanel.setBackground(Color.RED);
        timerPanel.add(timerLabel);
        jsp=new JScrollPane(pd,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel mainPanel=new JPanel();
        JPanel leftPanel=new JPanel();
        JFrame frame=new JFrame();
        frame.setTitle("Paint-Application");
        LineBorder lineBorder=new LineBorder(Color.RED,10);
        //cardPanel=new JPanel();
        //cardLayout=new CardLayout();
        //cardPanel.setLayout(cardLayout);
        //cardPanel.add(pd, "DrawingPage");
        //CutPage cutPage=new CutPage();
        //cardPanel.add(cutPage, "CutPage");
        //cardLayout.show(cardPanel, "DrawingPage");
        container=getContentPane();
        pd=new MyDrawingPad();
        pd.setBackground(Color.WHITE);
        button=new JButton("Hello");
        ImageIcon pencil=new ImageIcon("pencil.png");
        ImageIcon square=new ImageIcon("square.png");
        ImageIcon scissor=new ImageIcon("scissor.png");
        ImageIcon save=new ImageIcon("save.png");
        ImageIcon screenshotimage=new ImageIcon("screenshot.png");
        ImageIcon eraser=new ImageIcon("eraser.png");
        ImageIcon circle=new ImageIcon("circle.png");
        ImageIcon color=new ImageIcon("colors.png");
        Container container=getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel headerPanel=new JPanel();
        JPanel colorSectionPanel=new JPanel();
        JPanel buttonsPanel=new JPanel();
        JButton pencilButton=new JButton(pencil);
        JButton eraserButton=new JButton(eraser);
        JButton squareButton=new JButton(square);
        JButton circleButton=new JButton(circle);
        JButton textWrite=new JButton(scissor);
        JButton screenshot=new JButton(screenshotimage);
        JButton saveButton=new JButton(save);
        JButton colorGrading=new JButton(color);
        /* ****************** Made By Kamran Akthar ************************************* */
        pencilButton.setBackground(new Color(0,0,153));
        eraserButton.setBackground(new Color(0,0,153));
        squareButton.setBackground(new Color(0,0,153));
        circleButton.setBackground(new Color(0,0,153));
        textWrite.setBackground(new Color(0,0,153));
        screenshot.setBackground(new Color(0,0,153));
        saveButton.setBackground(new Color(0,0,153));
        colorGrading.setBackground(new Color(0,0,153));
        
        headerPanel.setBackground(Color.CYAN);
        mainPanel.setBackground(Color.RED);
        leftPanel.setBackground(Color.CYAN);
        //headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,4));
        pencilButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        eraserButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        squareButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        circleButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        textWrite.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        screenshot.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        colorGrading.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        headerPanel.setLayout(null);
        /* ****************** Made By Kamran Akthar ************************************* */
        headerPanel.add(pencilButton);
        headerPanel.add(eraserButton);
        headerPanel.add(squareButton);
        headerPanel.add(circleButton);
        headerPanel.add(textWrite);
        headerPanel.add(screenshot);
        headerPanel.add(saveButton);
        headerPanel.add(colorGrading);
        mainPanel.setBounds(1,9,1400,60);
        leftPanel.setBounds(-4,0,50,900);
        headerPanel.setBounds(7,25,35,325);
        colorSectionPanel.setBounds(10,400,50,200);
        pencilButton.setBounds(2,10,30,30);
        eraserButton.setBounds(2,130,30,30);
        squareButton.setBounds(2,50,30,30);
        circleButton.setBounds(2,170,30,30);
        textWrite.setBounds(2,90,30,30);
        screenshot.setBounds(2,210,30,30);
        saveButton.setBounds(2,250,30,30);
        colorGrading.setBounds(2, 290, 30,30);
        container.add(headerPanel);
        container.add(jsp);
        //container.add(mainPanel);
        setLayout(null);
        //ImageIcon red=new ImageIcon("red.png");
        //ImageIcon blue=new ImageIcon("blue.png");
        //ImageIcon green=new ImageIcon("green.png.");
        //ImageIcon black=new ImageIcon("black.jpg");
        //ImageIcon yellow=new ImageIcon("yellow.png");
        JPanel colorPanel=new JPanel();
        JButton redColor=new JButton();
        JButton greenColor=new JButton();
        JButton blackColor=new JButton();
        JButton blueColor=new JButton();
        JButton yellowColor=new JButton();
        redColor.setBackground(new Color(255,0,0));
        /* ****************** Made By Kamran Akthar ************************************* */
        greenColor.setBackground(new Color(0,204,0));
        blackColor.setBackground(new Color(0,0,0));
        blueColor.setBackground(new Color(0,0,255));
        yellowColor.setBackground(Color.YELLOW);
        colorPanel.setBackground(Color.CYAN);
        colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        redColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        greenColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        blackColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        blueColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        yellowColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setLayout(null);
        colorPanel.add(redColor);
        colorPanel.add(greenColor);
        colorPanel.add(blackColor);
        colorPanel.add(blueColor);
        colorPanel.add(yellowColor);
        colorPanel.setBounds(9,450,30,160);
        //redColor.setBounds(10, 460, 30, 30);
        redColor.setPreferredSize(new Dimension(25,25));
        greenColor.setPreferredSize(new Dimension(25,25));
        blackColor.setPreferredSize(new Dimension(25,25));
        blueColor.setPreferredSize(new Dimension(25,25));
        yellowColor.setPreferredSize(new Dimension(25,25));
        container.add(colorPanel);
        container.add(leftPanel);
        //frame.setSize(800,900);
        //setVisible(true);
        /* ****************** Made By Kamran Akthar ************************************* */
        pencilButton.addMouseListener(new ButtonTooltipMouseListener("pencil"));
        circleButton.addMouseListener(new ButtonTooltipMouseListener("circle"));
        squareButton.addMouseListener(new ButtonTooltipMouseListener("square"));
        saveButton.addMouseListener(new ButtonTooltipMouseListener("save"));
        eraserButton.addMouseListener(new ButtonTooltipMouseListener("eraser"));
        colorGrading.addMouseListener(new ButtonTooltipMouseListener("colors"));
        textWrite.addMouseListener(new ButtonTooltipMouseListener("cut"));
        pencilButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setPencilCursor();
                pd.resetCircle();
                pd.resetRectangle();
            }
        });
        /* ****************** Made By Kamran Akthar ************************************* */
        eraserButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setEraser();
            }
        });
        squareButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setDrawRectangle(!pd.drawRectangle);
                //pd.resetRectangle();
            }
        });
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                saveDrawing();
            }   
        });
        /* ************************************************************* */
        colorGrading.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(Drawing.this, "Choose Color", currentColor);
                if (newColor != null) {
                    currentColor = newColor;
                }
                pd.setFirstColor(newColor);
            }
        });
        /* ****************** Made By Kamran Akthar ************************************* */
        redColor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setFirstColor(Color.red);
            }
        });
        greenColor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setFirstColor(new Color(0,204,0));
            }
        });
        blueColor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setFirstColor(new Color(0,0,255));
            }
        });
        blackColor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setFirstColor(new Color(0,0,0));
            }
        });
        yellowColor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setFirstColor(Color.YELLOW);
            }
        });
        textWrite.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                clearDrawing();
                clearCircle();
            }
        });
        circleButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                pd.setDrawCircle(!pd.drawCircle);
            }
        });
        t=new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
                java.util.Date date=new java.util.Date();
                String nowString=sdf.format(date);
                timerLabel.setText(nowString);
            }
        });
        t.start();
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                int selectedOption=JOptionPane.showConfirmDialog(Drawing.this,"Do You Want To Save The File  ?");
                if(selectedOption==JOptionPane.YES_OPTION)
                {
                    saveDrawing();
                }
                if(selectedOption==JOptionPane.NO_OPTION)
                {
                    System.exit(0);
                }
                if(selectedOption==JOptionPane.CANCEL_OPTION)
                {
                }
                System.out.println(selectedOption);
            }
        });
        /* ****************** Made By Kamran Akthar ************************************* */
        container.setLayout(new BorderLayout());
        container.add(timerPanel, BorderLayout.SOUTH);
        container.add(pd,BorderLayout.CENTER);
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(100,100);
        setSize(screenSize.width, screenSize.height);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    private class ButtonTooltipMouseListener extends MouseAdapter
    {
        private String tooltiptext;
        public ButtonTooltipMouseListener(String tooltiptext)
        {
            this.tooltiptext=tooltiptext;
        }
        public void mouseEntered(MouseEvent e)
        {
            JButton button=(JButton) e.getSource();
            button.setToolTipText(tooltiptext);
        }
        public void mouseExited(MouseEvent e)
        {
            JButton button=(JButton) e.getSource();
            button.setToolTipText(null);
        }
    }
    private void clearDrawing()
    {
        pd.getPoints().clear();
        pd.repaint();
    }
    private void clearCircle()
    {
        pd.getCircle().clear();
        pd.repaint();
    }
    private void saveDrawing()
    {
        try 
        {
            // Create a BufferedImage to store the drawing
            BufferedImage image = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            // Draw the background color on the image
            g2d.setColor(pd.getBackground());
            g2d.fillRect(0, 0, pd.getWidth(), pd.getHeight());
            // Draw the drawing points on the image
            // Use JFileChooser to let the user choose the file location and name
            g2d.setColor(pd.getForeground());
            List<MyDrawingPad.Shape> shapes=pd.getShapes();
            for(MyDrawingPad.Shape shape:shapes)
            {
                /* ****************** Made By Kamran Akthar ************************************* */
                g2d.setColor(pd.getFirstColor());
                shape.draw(g2d);
            }
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(Drawing.this);
            if (result == JFileChooser.APPROVE_OPTION) 
            {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.toString();
                // Make sure the file has a .png extension
                if (!fileName.toLowerCase().endsWith(".png")) 
                {
                    fileName += ".png";
                }
                // Save the drawing as a PNG file
                File outputFile = new File(fileName);
                ImageIO.write(image, "png", outputFile);
                JOptionPane.showMessageDialog(Drawing.this, "Drawing saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(Drawing.this, "Error saving the drawing.", "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
}
/* ****************** Made By Kamran Akthar ************************************* */
class psp2
{
    public static void main(String gg[])
    {
        Drawing d=new Drawing();
    }
}
/* ****************** Made By Kamran Akthar ************************************* */