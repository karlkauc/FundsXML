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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.xmlbeans.XmlCursor;
import org.fundsxml.*;
import org.fundsxml.FundsXML4Document.FundsXML4.Funds;

import javax.xml.namespace.QName;
import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

import static org.fundsxml.FundDynamicDataType.Portfolios;
import static org.fundsxml.FundDynamicDataType.TotalAssetValues;

public class CreateFundsXMLFile {

    public static void main(String... args) {
        System.out.println("Creating FundsXML Document");

        final String fileName = "FundsXML_431.xml";

        // Random Volume figures
        float fundVolume = RandomUtils.nextFloat(1000000, 99999999);
        float position1Volume = RandomUtils.nextFloat(0, 1000000);
        float position2Volume = fundVolume - position1Volume;

        Calendar contentDate = Calendar.getInstance();
        contentDate.set(Calendar.YEAR, 2018);
        contentDate.set(Calendar.MONTH, 12);
        contentDate.set(Calendar.DATE, 28);

        // create new FundsXML Document Container
        FundsXML4Document fundsXML4Document = FundsXML4Document.Factory.newInstance();

        // create FundsXML4 root node
        FundsXML4Document.FundsXML4 fundsXML4 = fundsXML4Document.addNewFundsXML4();

        // add schema location to root node. needed for online schema validation.
        XmlCursor cursor = fundsXML4Document.newCursor();
        QName qName1 = new QName("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", "xsi");
        if (cursor.toFirstChild()) {
            cursor.setAttributeText(qName1, "http://schema.fundsxml.at/FundsXML_4.1.3.xsd");
        }

        // add mandatory node "ControlData"
        ControlDataType controlDataType = fundsXML4.addNewControlData();
        controlDataType.setUniqueDocumentID(RandomStringUtils.randomAlphabetic(12));
        controlDataType.setDocumentGenerated(Calendar.getInstance());
        controlDataType.setVersion(ControlDataType.Version.X_4_1_3);
        controlDataType.setContentDate(Calendar.getInstance());

        // add node "DataSupplier"
        DataSupplierType dataSupplierType = controlDataType.addNewDataSupplier();
        dataSupplierType.setSystemCountry("DE");
        dataSupplierType.setShort("EAM");
        dataSupplierType.setName("Asset Management Company");
        dataSupplierType.setType("AssetManager");
        controlDataType.setDataSupplier(dataSupplierType);

        // Add new Fund Node
        Funds funds = fundsXML4.addNewFunds();
        FundType fundType = funds.addNewFund();
        IdentifiersType identifiersType = fundType.addNewIdentifiers();
        identifiersType.setLEI("5299003BGS3OLPA5YE94");
        fundType.addNewNames().setOfficialName("OFFICIAL FUND NAME");
        fundType.setCurrency("EUR");
        fundType.setSingleFundFlag(true);

        FundDynamicDataType fundDynamicDataType = fundType.addNewFundDynamicData();

        // Total New Asset Node (FundVolume)
        TotalAssetValues totalAssetValues = fundDynamicDataType.addNewTotalAssetValues();
        TotalAssetValueType totalAssetValueType = totalAssetValues.addNewTotalAssetValue();
        totalAssetValueType.setNavDate(contentDate);
        totalAssetValueType.setTotalAssetNature(TotalAssetValueType.TotalAssetNature.OFFICIAL);
        FundAmountType fundAmountType = totalAssetValueType.addNewTotalNetAssetValue();
        FundAmountType.Amount amount = fundAmountType.addNewAmount();
        amount.setCcy("EUR");
        amount.setIsFundCcy(true);
        amount.setBigDecimalValue(new BigDecimal(fundVolume));

        // Portfolio
        Portfolios portfolios = fundDynamicDataType.addNewPortfolios();
        PortfolioType portfolioType = portfolios.addNewPortfolio();
        portfolioType.setNavDate(contentDate);

        // every position has to be reflected in the asset master data
        // so we need both. position and asset master data
        PositionsType positionsType = portfolioType.addNewPositions();
        AssetMasterDataType assetMasterDataType = fundsXML4.addNewAssetMasterData();


        // 1st instument in fund:
        // UniqueID of this instument - used in position and master data
        String uniqueID1 = RandomStringUtils.randomAlphabetic(12);

        PositionType position1 = positionsType.addNewPosition();
        AssetType asset1 = assetMasterDataType.addNewAsset();

        // 1st instrument in portfolio node
        position1.setUniqueID(uniqueID1);
        FundAmountType totalValPos1 = position1.addNewTotalValue();
        FundAmountType.Amount pos1Amount = totalValPos1.addNewAmount();
        pos1Amount.setBigDecimalValue(new BigDecimal(position1Volume));
        pos1Amount.setCcy("EUR");
        position1.addNewBond().setNominal(new BigDecimal(100));

        // 1st position in asset master data node
        asset1.setUniqueID(uniqueID1);
        asset1.setCurrency("EUR");
        asset1.setName("Bond 1");
        asset1.setAssetType(AssetType.AssetType2.BO);
        asset1.addNewAssetDetails().addNewBond().setConvertibleFlag(false);

        // 2nd instument in fund:
        String uniqueID2 = RandomStringUtils.randomAlphabetic(12);
        PositionType position2 = positionsType.addNewPosition();
        AssetType asset2 = assetMasterDataType.addNewAsset();

        // 2nd instrument in portfolio node
        // it is also possible to add noded in a more direct way:

        position2.setUniqueID(uniqueID2);
        FundAmountType totalValPos2 = position2.addNewTotalValue();
        FundAmountType.Amount pos2Amount = totalValPos2.addNewAmount();
        pos2Amount.setBigDecimalValue(new BigDecimal(position2Volume));
        pos2Amount.setCcy("EUR");
        position2.addNewEquity().setUnits(new BigDecimal(333));

        // 2nd position in asset master data node
        asset2.setUniqueID(uniqueID2);
        asset2.setCurrency("EUR");
        asset2.setName("Equity 1");
        asset2.setAssetType(AssetType.AssetType2.EQ);

        asset2.addNewAssetDetails().addNewEquity(); // add emtpy Equity Node
        asset2.getAssetDetails().getEquity().addNewIssuer(); // add empty Issuer Node
        asset2.getAssetDetails().getEquity().getIssuer().setName("Erste Group Bank AG"); // set the "Name" in the new added Issuer Node
        asset2.getAssetDetails().getEquity().getIssuer().addNewIdentifiers().setLEI("PQOH26KWDF7CG10L6792"); // add new Node "Identifiers" and add new node LEI with value ".."
        asset2.getAssetDetails().getEquity().getIssuer().getIdentifiers().setISIN("AT0000652011"); // add node ISIN in the new added Identifiers node

        System.out.println(fundsXML4Document);
        System.out.println("Schema valid?: " + fundsXML4Document.validate());

        try {
            fundsXML4Document.save(new File(fileName));
            System.out.println("File [" + fileName + "] saved.");
        } catch (Exception e) {
            System.out.println("Error in saving File.");
            e.printStackTrace();
        }
    }
}
