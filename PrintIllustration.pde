final int textSize = 18;
int gapX = 0;
int gapY = 0;
ArrayList<Block> blockList = new ArrayList<Block>();

Argument testArgument = new Argument("String", "result");
Method testMethod;
void setup(){
    size(1200, 900);
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

void draw(){
    background(255);
    translate(gapX,gapY);

    for(Block block : blockList){
        block.draw();
    }
}

void mouseDragged(){
    gapX += mouseX - pmouseX;
    gapY += mouseY - pmouseY;
}

void keyPressed(){
    if(key == 'r'){
        gapX = 0;
        gapY = 0;
    }
}

int sum(int x, int y){
    //以下自動生成されたプログラムです
    String typeName = new Object(){}.getClass().getEnclosingMethod().getReturnType().getName();
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    ArrayList<Argument> argList = new ArrayList<Argument>();
    for(Class param : new Object(){}.getClass().getEnclosingMethod().getParameterTypes()){
        argList.add(new Argument(param.getName(), "temp")); //仮引数のの名前を取得できなかったのでtempで代用
    }
    blockList.add(new Method(typeName, methodName, argList));
    //ここまで自動生成されたプログラム

    return x + y;
}

class AnotationClass extends Block{
    ArrayList<Argument> argList;
    ArrayList<Method> methodList;
    void draw(){

    }
}

public abstract class Block {
    protected float x, y;
    protected float width, height;
    String typeName;
    String name;
    protected color fillColor;
    protected color textColor;

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
    void draw(){
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
    void draw(){
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
