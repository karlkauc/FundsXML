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

import org.apache.commons.lang3.StringUtils;
import org.fundsxml.*;

import java.io.File;

public class ReadFundsXMLFile {

    public static void main(String... args) {
        System.out.println("Reading FundsXML Document");

        File inputXMLFile = new File("FundsXML_431.xml");
        try {
            FundsXML4Document fundsXML4Document = FundsXML4Document.Factory.parse(inputXMLFile);
            FundsXML4Document.FundsXML4 fundsXML4 = fundsXML4Document.getFundsXML4();

            // only for formating the output!
            int headerPad = 22;

            System.out.println(System.lineSeparator() + "Found document [" + inputXMLFile + "]");
            System.out.println(StringUtils.rightPad("Document Unique ID", headerPad, '.') + ": " + fundsXML4.getControlData().getUniqueDocumentID());
            System.out.println(StringUtils.rightPad("FundsXML Version", headerPad, '.') + ": " + fundsXML4.getControlData().getVersion());
            System.out.println(StringUtils.rightPad("DS Name", headerPad, '.') + ": " + fundsXML4.getControlData().getDataSupplier().getName());
            System.out.println(StringUtils.rightPad("Document Generated", headerPad, '.') + ": " + fundsXML4.getControlData().getContentDate());
            System.out.println(StringUtils.rightPad("Schema Valid", headerPad, '.') + ": " + fundsXML4Document.validate());

            FundType[] funds = fundsXML4.getFunds().getFundArray();
            if (funds.length > 0) { // Found at least one fund in FundsXML File
                System.out.println(System.lineSeparator() + "Found [" + funds.length + "] fund:");
            }
            else {
                System.out.println("No fund found in FundsXML File");
            }

            // only for formating the output!
            int fundPad = 30;

            for (FundType fund : funds) {
                System.out.println(StringUtils.rightPad("LEI", fundPad, '.') + ": " + fund.getIdentifiers().getLEI());
                System.out.println(StringUtils.rightPad("Name", fundPad, '.') + ": " + fund.getNames().getOfficialName());
                System.out.println(StringUtils.rightPad("CCY", fundPad, '.') + ": " + fund.getCurrency());

                // could be more than one fund volume (total net asset value) data. so we need to loop ofer the TotalAssetValueArray
                for (TotalAssetValueType totalAssetValueType : fund.getFundDynamicData().getTotalAssetValues().getTotalAssetValueArray()) {
                    System.out.println(StringUtils.rightPad("Fund NAV Date", fundPad, '.') + ": " + totalAssetValueType.getNavDate());

                    // if you are sure that only one value exists! otherwise loop over the array.
                    System.out.println(StringUtils.rightPad("Fund NAV Value", fundPad, '.') + ": " + totalAssetValueType.getTotalNetAssetValue().getAmountArray(0).getStringValue());
                }

                int holdingsPad = fundPad + 10; // only for formating


                // print fund holdings
                for (PortfolioType portfolioType : fund.getFundDynamicData().getPortfolios().getPortfolioArray()) {
                    System.out.println(StringUtils.rightPad("Position NAV Date", holdingsPad, '.') + ": " + portfolioType.getNavDate());

                    AssetType[] assetArray = fundsXML4.getAssetMasterData().getAssetArray();

                    int positionCount = 0;
                    for (PositionType positionType : portfolioType.getPositions().getPositionArray()) {
                        System.out.println(StringUtils.rightPad("[" + positionCount + "] Position Unique ID", holdingsPad, '.') + ": " + positionType.getUniqueID());


                        for (AssetType assetType: assetArray) {
                            if (assetType.getUniqueID().equals(positionType.getUniqueID())) {
                                System.out.println(StringUtils.rightPad("[" + positionCount + "] Position Asset Type", holdingsPad, '.') + ": " + assetType.getAssetType());
                                System.out.println(StringUtils.rightPad("[" + positionCount + "] Position Name", holdingsPad, '.') + ": " + assetType.getName());
                            }
                        }
                        System.out.println(StringUtils.rightPad("[" + positionCount + "] Position Volume", holdingsPad, '.') + ": " + positionType.getTotalValue().getAmountArray(0).getStringValue());

                        positionCount++;
                        System.out.println(StringUtils.rightPad("", holdingsPad, "-"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
