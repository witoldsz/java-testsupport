package com.danhaywood.testsupport.jmock;

import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.ClassUnderTest;
import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.Ignoring;
import com.danhaywood.testsupport.jmock.JUnitRuleMockery2.Mode;

public class JUnitRuleMockery2Test_mockAnnotatedWithIgnoring {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Ignoring
    @Mock
    private Collaborator collaborator;

    @ClassUnderTest
	private Collaborating collaborating;

    @Before
	public void setUp() throws Exception {
    	collaborating = (Collaborating) context.getClassUnderTest();
	}
    
    @Test
    public void invocationOnCollaboratorIsIgnored() {
    	collaborating.collaborateWithCollaborator();
    }

    @Test
    public void lackOfInvocationOnCollaboratorIsIgnored() {
    	collaborating.dontCollaborateWithCollaborator();
    }

}
