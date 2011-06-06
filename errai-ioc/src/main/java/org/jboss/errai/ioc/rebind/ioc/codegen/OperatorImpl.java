package org.jboss.errai.ioc.rebind.ioc.codegen;

import org.jboss.errai.ioc.rebind.ioc.codegen.exception.InvalidExpressionException;
import org.jboss.errai.ioc.rebind.ioc.codegen.meta.MetaClass;

/**
 * @author Mike Brock <cbrock@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class OperatorImpl implements Operator {
   
    private final String canonicalString;
    private final int operatorPrecedence;
    private final MetaClass[] lhsConstraints;
    private final MetaClass[] rhsConstraints;
    
    OperatorImpl(String canonicalString, int operatorPrecedence, Class<?>... constraints) {
        this.canonicalString = canonicalString;
        this.operatorPrecedence = operatorPrecedence;
        this.lhsConstraints = MetaClassFactory.fromClassArray(constraints);
        this.rhsConstraints = lhsConstraints;
    }

    OperatorImpl(String canonicalString, int operatorPrecedence, Class<?>[] lhsConstraints, Class<?>[] rhsConstraints) {
        this.canonicalString = canonicalString;
        this.operatorPrecedence = operatorPrecedence;
        this.lhsConstraints = MetaClassFactory.fromClassArray(lhsConstraints);
        this.rhsConstraints = MetaClassFactory.fromClassArray(rhsConstraints);
    }
    
    public String getCanonicalString() {
        return canonicalString;
    }

    public int getOperatorPrecedence() {
        return operatorPrecedence;
    }

    public boolean isHigherPrecedenceThan(Operator operator) {
        return operator.getOperatorPrecedence() < getOperatorPrecedence();
    }

    public boolean isEqualOrHigherPrecedenceThan(Operator operator) {
        return operator.getOperatorPrecedence() <= getOperatorPrecedence();
    }
    
    public void canBeApplied(MetaClass clazz) {
        canBeAppliedLhs(clazz);
    }
    
    public void canBeAppliedLhs(MetaClass clazz) {
        if (!canBeApplied(clazz, lhsConstraints)) {
            throw new InvalidExpressionException("Not a valid lhs type for operator '" + 
                    canonicalString + "':" + clazz.getFullyQualifedName());
        }
    }
  
    public void canBeAppliedRhs(MetaClass clazz) {
        if (!canBeApplied(clazz, rhsConstraints)) {
            throw new InvalidExpressionException("Not a valid rhs type for operator '" + 
                    canonicalString + "':" + clazz.getFullyQualifedName());
        }
    }
  
    private boolean canBeApplied(MetaClass clazz, MetaClass[] constraints) {
        if (constraints.length==0) return true;
        
        for (MetaClass mc : constraints) {
            if(mc.isAssignableFrom(clazz)) return true;
        }

        return false;
    }
}