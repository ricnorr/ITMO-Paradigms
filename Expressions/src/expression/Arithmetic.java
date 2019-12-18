package expression;

public abstract class Arithmetic implements MyExpression {
    private MyExpression leftOperand;
    private MyExpression rightOperand;

    public Arithmetic(MyExpression leftOperand, MyExpression rightOperand) {
       this.leftOperand = leftOperand;
       this.rightOperand = rightOperand;
   }

   protected abstract int calculateOperation(int leftOperand, int rightOperand);
   protected abstract double calculateOperation(double leftOperand, double rightOperand);
   protected abstract String getStringOperation();

   public int evaluate(int x) {
       return calculateOperation(leftOperand.evaluate(x), rightOperand.evaluate(x));
   }

    public double evaluate(double x) {
        return calculateOperation(leftOperand.evaluate(x), rightOperand.evaluate(x));
    }

   public String toString() {
       return "(" + leftOperand + " " + getStringOperation() + " " + rightOperand + ")";
   }

   public boolean equals(Object x) {
       if (x != null && this.getClass() == x.getClass()) {
           Arithmetic temp = (Arithmetic) x;
           return leftOperand.equals(temp.leftOperand) && rightOperand.equals(temp.rightOperand);
       }
       return false;
   }

   public int hashCode() {
       int result = 31 * leftOperand.hashCode();
       result = 31 * result + rightOperand.hashCode();
       return 31 * result + getClass().hashCode();
   }
}
