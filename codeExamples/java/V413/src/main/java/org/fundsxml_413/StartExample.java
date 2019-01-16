/*
 * Copyright 2019 Karl Kauc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.fundsxml_413;

import com.google.common.base.Stopwatch;

public class StartExample {

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        CreateFundsXMLFile.main();
        ReadFundsXMLFile.main();

        stopwatch.stop();
        System.out.println("time: " + stopwatch);

    }
}
