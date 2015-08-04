package charttest;


import javax.swing.table.AbstractTableModel;


public class SizeDistributionTableModel extends AbstractTableModel {
    public static final int RETAINED_COLUMN_INDEX = 0;
    public static final int CUMULATIVE_COLUMN_INDEX = 1;
    
    public static final float[] Fractions = {254.0f, 127.0f, 50.8f, 38.1f, 25.4f, 19.05f, 
        12.7f, 9.525f, 6.35f, 4.7625f, 3.175f, 2.0f, 1.397f, 1.168f, 0.991f, 0.589f, 0.495f,
        0.4f, 0.295f, 0.246f, 0.147f, 0.104f, 0.074f, 0.053f, 0.044f, 0.0f};
    
    public static final String[] d_columnNames = {"Retained (mm)", "Cumulative (%)"};
    
    private SizeDistribution d_sizeDistro = null;
    
    public SizeDistributionTableModel(SizeDistribution s) {
        d_sizeDistro = s;
        for(int i = 1; i < Fractions.length; i++) {
            this.addNewRow(Fractions[i]);
        } 
    }
    
    public final void addNewRow(float retained) {
        d_sizeDistro.setItems(retained, 0.0f, true);
//        d_sizeDistro.d_sizeList.add(retained);
//        d_sizeDistro.d_cumulativeWt.add(0.0f);
    }
    
    public SizeDistribution getSizeDistro(){
        return d_sizeDistro;
    }
    
    
    @Override
    public boolean isCellEditable(int row, int column) {
        switch(column) {
            case RETAINED_COLUMN_INDEX:
            case CUMULATIVE_COLUMN_INDEX:
                return true;
        } 
        return false;
     }
    
    @Override
    public String getColumnName(int column) {
        return d_columnNames[column];
    }
    
    @Override
    public Class getColumnClass(int column) {
        switch(column) {
            case RETAINED_COLUMN_INDEX:
            case CUMULATIVE_COLUMN_INDEX:
                return Float.class;
            default:
                return Object.class;
        }
    }
    
    @Override
    public Object getValueAt(int row, int column) {
         switch (column) {
             case RETAINED_COLUMN_INDEX:
                return d_sizeDistro.d_sizeList.get(row);
             case CUMULATIVE_COLUMN_INDEX:
                return d_sizeDistro.d_cumulativeWt.get(row);
             default:
                return new Object();
         }
     }
    
    @Override
    public void setValueAt(Object value, int row, int column) {
         switch (column) {
             case RETAINED_COLUMN_INDEX:
                d_sizeDistro.setSize(row, (float)value);
                break;
             case CUMULATIVE_COLUMN_INDEX:
                d_sizeDistro.setCumulativeWt(row, (float)value);
                break;
             default:
                System.out.println("invalid index");
         }
         fireTableCellUpdated(row, column);
    }
    
    @Override
    public int getColumnCount() {
         return d_columnNames.length;
     }
    
    @Override
    public int getRowCount() {
        return d_sizeDistro.d_cumulativeWt.size();
    }
    
}
