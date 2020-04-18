package algebra.nodes.basic;

import algebra.nodes.Dimension;
import algebra.nodes.Node;
import algebra.nodes.NodeCount;
import neuralnetwork.builder.BuildException;

public class Add extends Node<Add> {

    public Add(Node... subChilds) {
        super(NodeCount.UNLIMITED, NodeCount.UNLIMITED);
        for(Node n:subChilds){
            this.addPreviousNode(n);
        }
    }

    @Override
    protected Dimension selfCalcOutputDim() throws BuildException {
        if(!sameDimension(getPreviousNodes().toArray(new Node[]{}))){
            throw new BuildException(this, "Only accepting inputs with same dimension");
        }
        return new Dimension(getPreviousNode().getOutputDimension());
    }

    @Override
    protected void selfInit() {

    }

    @Override
    public void calc() {
        outputValue.reset(0);
        for(int i = 0; i < getPreviousNodes().size(); i++){
            outputDerivative[i].reset(1);
        }
        for(Node n:getPreviousNodes()){
            outputValue.self_add(n.getOutputValue());
        }
    }

    @Override
    public void autoDiff() {
        for(Node n:getPreviousNodes()){
            n.getOutputGradient().self_add(this.outputGradient);
        }
    }

    @Override
    public Add copy() {
        Add add = new Add();
        for(Node n:getPreviousNodesCopy()){
            add.addPreviousNode(n);
        }
        return add;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for(Node c:getPreviousNodes()){
            builder.append(c.toString());
            if(c != getPreviousNodes().get(getPreviousNodes().size()-1)){
                builder.append("+");
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
