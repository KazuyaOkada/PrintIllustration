import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PrintIllustration extends PApplet {

final int textSize = 18;
int gapX = 0;
int gapY = 0;
ArrayList<Block> blockList = new ArrayList<Block>();

Argument testArgument = new Argument("String", "result");
Method testMethod;
public void setup(){
    
    ArrayList<Argument> argList = new ArrayList<Argument>();
    argList.add(new Argument("int", "x"));
    argList.add(new Argument("int", "y"));
    argList.add(new Argument("String", "sum"));
    testMethod = new Method("int", "sum", argList);
    blockList.add(testArgument);
    blockList.add(testMethod);

    sum(10,20);

    textSize(textSize);
}

public void draw(){
    background(255);
    translate(gapX,gapY);

    for(Block block : blockList){
        block.draw();
    }
}

public void mouseDragged(){
    gapX += mouseX - pmouseX;
    gapY += mouseY - pmouseY;
}

public void keyPressed(){
    if(key == 'r'){
        gapX = 0;
        gapY = 0;
    }
}

public int sum(int x, int y){
    //\u4ee5\u4e0b\u81ea\u52d5\u751f\u6210\u3055\u308c\u305f\u30d7\u30ed\u30b0\u30e9\u30e0\u3067\u3059
    String typeName = new Object(){}.getClass().getEnclosingMethod().getReturnType().getName();
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    ArrayList<Argument> argList = new ArrayList<Argument>();
    for(Class param : new Object(){}.getClass().getEnclosingMethod().getParameterTypes()){
        argList.add(new Argument(param.getName(), "temp")); //\u4eee\u5f15\u6570\u306e\u306e\u540d\u524d\u3092\u53d6\u5f97\u3067\u304d\u306a\u304b\u3063\u305f\u306e\u3067temp\u3067\u4ee3\u7528
    }
    blockList.add(new Method(typeName, methodName, argList));
    //\u3053\u3053\u307e\u3067\u81ea\u52d5\u751f\u6210\u3055\u308c\u305f\u30d7\u30ed\u30b0\u30e9\u30e0

    return x + y;
}

class AnotationClass extends Block{
    ArrayList<Argument> argList;
    ArrayList<Method> methodList;
    public void draw(){

    }
}

public abstract class Block {
    protected float x, y;
    protected float width, height;
    String typeName;
    String name;
    protected int fillColor;
    protected int textColor;

    protected int xMargin = 5;
    protected int yMargin = 3;

    abstract public void draw();
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setWidth(float width){
        this.width = width;
    }
    public void setHeight(float height){
        this.height = height;
    }
}

class Argument extends Block{
    Argument(String typeName, String name){
        this.typeName = typeName;
        this.name = name;

        if(blockList.isEmpty()){
            x = 10;
        }else{
            float maxX = 0;
            for(Block block : blockList){
                float bx = block.getX() + block.getWidth();
                if(maxX < bx) maxX = bx;
            }
            x = maxX + xMargin;
        }

        y = 10;
        String a = typeName + " : " + name;
        width =  a.length() * 10 +  xMargin * 2;
        height = textSize + yMargin * 2;
        fillColor = color(52,152,219);
        textColor = color(0,0,0);
        blockList.add(this);
    }
    public void draw(){
        fill(fillColor);
        rect(x, y, width, height, 5);
        fill(textColor);
        textAlign(LEFT, TOP);
        text(typeName + " : " + name, x + xMargin, y + yMargin);
    }
}

class Method extends Block {
    ArrayList<Argument> argList = new ArrayList<Argument>();
    Method(String typeName, String name){
        this.typeName = typeName;
        this.name = name;

        x = 10;
        y = 10;
        setWidth();
        height = textSize + yMargin * 2;
        fillColor = color(231,76,60);
        textColor = color(0,0,0);
        blockList.add(this);
    }
    Method(String typeName, String name, ArrayList<Argument> argList){
        this(typeName, name);
        this.argList = argList;
        float posY = height;
        for(Argument arg : argList){
            blockList.remove(arg);
        }
        if(blockList.isEmpty()){
            x = 10;
        }else{
            float maxX = 0;
            for(Block block : blockList){
                float bx = block.getX() + block.getWidth();
                if(maxX < bx) maxX = bx;
            }
            x = maxX + xMargin;
        }
        for(Argument arg : argList){
            arg.setX(x + xMargin);
            arg.setY(y + posY);
            posY += yMargin;
            posY += arg.getHeight();
        }

        setWidth();
        setHeight();
    }
    public void draw(){
        fill(fillColor);
        rect(x, y, width, height, 5);
        fill(textColor);
        textAlign(LEFT, TOP);
        text(typeName + " : " + name, x + xMargin, y + yMargin);

        for(Argument arg : argList){
            arg.draw();
        }
    }
    private void setWidth(){
        String a = typeName + " : " + name;
        width =  a.length() * 10 +  xMargin * 2;

        float maxArgWidth = 0;
        for(Argument arg : argList){
            if(maxArgWidth < arg.getWidth()) maxArgWidth = arg.getWidth();
        }
        if(width < maxArgWidth + xMargin * 2) width = maxArgWidth + xMargin * 2;
    }
    private void setHeight(){
        height = textSize + yMargin * 2;

        for(Argument arg : argList){
            height += arg.getHeight();
            height += yMargin;
        }
    }
}
    public void settings() {  size(1200, 900); }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "--present", "--window-color=#323232", "--stop-color=#FF0000", "PrintIllustration" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
