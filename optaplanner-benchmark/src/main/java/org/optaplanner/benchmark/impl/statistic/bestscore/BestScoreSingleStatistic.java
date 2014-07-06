/*
 * Copyright 2010 JBoss Inc
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

package org.optaplanner.benchmark.impl.statistic.bestscore;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.benchmark.impl.result.SingleBenchmarkResult;
import org.optaplanner.benchmark.config.statistic.ProblemStatisticType;
import org.optaplanner.benchmark.impl.statistic.ProblemBasedSingleStatistic;
import org.optaplanner.benchmark.impl.statistic.SingleStatistic;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.impl.score.definition.ScoreDefinition;
import org.optaplanner.core.api.domain.solution.Solution;

public class BestScoreSingleStatistic extends ProblemBasedSingleStatistic<BestScoreStatisticPoint> {

    private final BestScoreSingleStatisticListener listener;

    public BestScoreSingleStatistic(SingleBenchmarkResult singleBenchmarkResult) {
        super(singleBenchmarkResult, ProblemStatisticType.BEST_SCORE);
        listener = new BestScoreSingleStatisticListener();
    }

    // ************************************************************************
    // Lifecycle methods
    // ************************************************************************

    public void open(Solver solver) {
        solver.addEventListener(listener);
    }

    public void close(Solver solver) {
        solver.removeEventListener(listener);
    }

    private class BestScoreSingleStatisticListener implements SolverEventListener<Solution> {

        public void bestSolutionChanged(BestSolutionChangedEvent<Solution> event) {
            pointList.add(new BestScoreStatisticPoint(
                    event.getTimeMillisSpent(), event.getNewBestSolution().getScore()));
        }

    }

    // ************************************************************************
    // CSV methods
    // ************************************************************************

    @Override
    protected String getCsvHeader() {
        return BestScoreStatisticPoint.buildCsvLine("timeMillisSpent", "score");
    }

    @Override
    protected BestScoreStatisticPoint createPointFromCsvLine(ScoreDefinition scoreDefinition,
            List<String> csvLine) {
        return new BestScoreStatisticPoint(Long.valueOf(csvLine.get(0)),
                scoreDefinition.parseScore(csvLine.get(1)));
    }

}
