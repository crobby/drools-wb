/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.scenariosimulation.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.drools.workbench.screens.scenariosimulation.client.factories.ScenarioCellTextBoxSingletonDOMElementFactory;
import org.drools.workbench.screens.scenariosimulation.client.factories.ScenarioHeaderTextBoxSingletonDOMElementFactory;
import org.drools.workbench.screens.scenariosimulation.client.metadata.ScenarioHeaderMetaData;
import org.drools.workbench.screens.scenariosimulation.client.renderers.ScenarioGridColumnRenderer;
import org.drools.workbench.screens.scenariosimulation.client.widgets.ScenarioGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;

public class ScenarioSimulationBuilders {

    /**
     * <b>Builder</b> for <code>ScenarioGridColumn</code>.
     * <p>
     * If not provided, default values are:
     * <p>
     * width: 150
     * </p>
     * <p>
     * isMovable: <code>false</code>;
     * </p>
     * <p>
     * isReadOnly: <code>true</code>;
     * </p>
     * <p>
     * placeHolder: "";
     * </p>
     * <p>
     * columnRenderer: new ScenarioGridColumnRenderer()
     * </p>
     * <p>
     * headerMetaData: new ArrayList<>
     * </p>
     * </p>
     */
    public static class ScenarioGridColumnBuilder {

        private double width = 150;
        private boolean isMovable = false;
        private boolean isReadOnly = true;
        private String placeHolder = "";
        private final HeaderBuilder headerBuilder;
        private ScenarioGridColumnRenderer scenarioGridColumnRenderer;
        private final ScenarioCellTextBoxSingletonDOMElementFactory factoryCell;

        public static ScenarioGridColumnBuilder get(ScenarioCellTextBoxSingletonDOMElementFactory factoryCell, HeaderBuilder headerBuilder) {
            return new ScenarioGridColumnBuilder(factoryCell, headerBuilder);
        }

        private ScenarioGridColumnBuilder(ScenarioCellTextBoxSingletonDOMElementFactory factoryCell, HeaderBuilder headerBuilder) {
            this.factoryCell = factoryCell;
            this.headerBuilder = headerBuilder;
        }

        public ScenarioGridColumnBuilder setWidth(double width) {
            this.width = width;
            return this;
        }

        public ScenarioGridColumnBuilder setMovable(boolean movable) {
            isMovable = movable;
            return this;
        }

        public ScenarioGridColumnBuilder setReadOnly(boolean readOnly) {
            isReadOnly = readOnly;
            return this;
        }

        public ScenarioGridColumnBuilder setPlaceHolder(String placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public ScenarioGridColumnBuilder setScenarioGridColumnRenderer(ScenarioGridColumnRenderer scenarioGridColumnRenderer) {
            this.scenarioGridColumnRenderer = scenarioGridColumnRenderer;
            return this;
        }

        public ScenarioGridColumn build() {
            List<GridColumn.HeaderMetaData> headerMetaDataList =  headerBuilder.build();
            ScenarioGridColumnRenderer actualScenarioGridColumnRenderer = scenarioGridColumnRenderer != null ? scenarioGridColumnRenderer : new ScenarioGridColumnRenderer();
            ScenarioGridColumn toReturn = new ScenarioGridColumn(headerMetaDataList,
                                                                 actualScenarioGridColumnRenderer,
                                                                 width,
                                                                 isMovable,
                                                                 factoryCell,
                                                                 placeHolder
            );
            toReturn.setReadOnly(isReadOnly);
            return toReturn;
        }
    }

    /**
     * <b>Builder</b> for <code>List&lt;GridColumn.HeaderMetaData&gt;</code>.
     * <p>
     * If not provided, default values are:
     * <p>
     * columnId: null
     * </p>
     * <p>
     * columnTitle: null;
     * </p>
     * <p>
     * isReadOnly: <code>false</code>;
     * </p>
     * <p>
     * informationHeader: <code>false</code>;
     * </p>
     * </p>
     */
    public static class HeaderBuilder {

        String columnId;
        String columnTitle;
        String columnGroup = "";
        boolean readOnly = false;
        boolean informationHeader = false;
        HeaderBuilder nestedLevel;
        final ScenarioHeaderTextBoxSingletonDOMElementFactory factory;

        public static HeaderBuilder get(ScenarioHeaderTextBoxSingletonDOMElementFactory factory) {
            return new HeaderBuilder(factory);
        }

        public HeaderBuilder(ScenarioHeaderTextBoxSingletonDOMElementFactory factory) {
            this.factory = factory;
        }

        public String getColumnId() {
            return columnId;
        }

        public HeaderBuilder setColumnId(String columnId) {
            this.columnId = columnId;
            return this;
        }

        public HeaderBuilder setColumnTitle(String columnTitle) {
            this.columnTitle = columnTitle;
            return this;
        }

        public HeaderBuilder setColumnGroup(String columnGroup) {
            this.columnGroup = columnGroup;
            return this;
        }

        public HeaderBuilder setReadOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }

        public HeaderBuilder setInformationHeader(boolean informationHeader) {
            this.informationHeader = informationHeader;
            return this;
        }

        public HeaderBuilder newLevel() {
            this.nestedLevel = HeaderBuilder.get(factory)
                    .setColumnId(columnId)
                    .setColumnTitle(columnTitle)
                    .setColumnGroup(columnGroup)
                    .setReadOnly(readOnly);
            return this.nestedLevel;
        }

        public List<GridColumn.HeaderMetaData> build() {
            List<GridColumn.HeaderMetaData> toReturn = new ArrayList<>();
            HeaderBuilder current = this;
            do {
                toReturn.add(current.internalBuild());
                current = current.nestedLevel;
            } while (current != null);
            return toReturn;
        }

        private GridColumn.HeaderMetaData internalBuild() {
            return new ScenarioHeaderMetaData(columnId, columnTitle, columnGroup, factory, readOnly, informationHeader);
        }
    }
}
