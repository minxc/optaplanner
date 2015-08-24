/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.api.score.buildin.hardsoftlong;

import org.junit.Test;
import org.kie.api.runtime.rule.RuleContext;
import org.optaplanner.core.api.score.holder.AbstractScoreHolderTest;

import static org.junit.Assert.*;

public class HardSoftLongScoreHolderTest extends AbstractScoreHolderTest {

    @Test
    public void addConstraintMatchWithConstraintMatch() {
        addConstraintMatch(true);
    }

    @Test
    public void addConstraintMatchWithoutConstraintMatch() {
        addConstraintMatch(false);
    }

    public void addConstraintMatch(boolean constraintMatchEnabled) {
        HardSoftLongScoreHolder scoreHolder = new HardSoftLongScoreHolder(constraintMatchEnabled);

        scoreHolder.addHardConstraintMatch(mockRuleContext("scoreRule1"), -1000L);

        RuleContext ruleContext2 = mockRuleContext("scoreRule2");
        scoreHolder.addHardConstraintMatch(ruleContext2, -200L);
        callUnMatch(ruleContext2);

        RuleContext ruleContext3 = mockRuleContext("scoreRule3");
        scoreHolder.addSoftConstraintMatch(ruleContext3, -30L);
        scoreHolder.addSoftConstraintMatch(ruleContext3, -3L); // Overwrite existing
        scoreHolder.addHardConstraintMatch(ruleContext3, -300L); // Different score level
        scoreHolder.addHardConstraintMatch(ruleContext3, -400L); // Overwrite existing

        RuleContext ruleContext4 = mockRuleContext("scoreRule4");
        scoreHolder.addHardConstraintMatch(ruleContext4, -1);
        scoreHolder.addSoftConstraintMatch(ruleContext4, -1);
        callUnMatch(ruleContext4);

        assertEquals(HardSoftLongScore.valueOf(-1400L, -3L), scoreHolder.extractScore());
        if (constraintMatchEnabled) {
            assertEquals(6, scoreHolder.getConstraintMatchTotals().size());
        }
    }

}
