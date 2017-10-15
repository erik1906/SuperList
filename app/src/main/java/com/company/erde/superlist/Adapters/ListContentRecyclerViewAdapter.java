package com.company.erde.superlist.Adapters;

/**
 * Created by Erik on 12/10/2017.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.erde.superlist.R;
import com.company.erde.superlist.RealModels.ListProducts;
import com.company.erde.superlist.RealModels.Product;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Erik on 11/10/2017.
 */

public class ListContentRecyclerViewAdapter<contentList extends ListProducts, rowViewHolderList extends ListContentRecyclerViewAdapter.ViewHolder>
        extends RecyclerView.Adapter<ListContentRecyclerViewAdapter.ViewHolder> {

    private final boolean hasAutoUpdates;
    private final boolean updateOnModification;
    private final OrderedRealmCollectionChangeListener listener;
    private final RecyclerViewClickListener recyclerViewClickListenerlistener;
    @Nullable
    private OrderedRealmCollection<contentList> adapterData;
    private int selectedPos;

    private OrderedRealmCollectionChangeListener createListener() {
        return new OrderedRealmCollectionChangeListener() {
            @Override
            public void onChange(Object collection, OrderedCollectionChangeSet changeSet) {
                // null Changes means the async query returns the first time.
                if (changeSet == null) {
                    notifyDataSetChanged();
                    return;
                }
                // For deletions, the adapter has to be notified in reverse order.
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (int i = deletions.length - 1; i >= 0; i--) {
                    OrderedCollectionChangeSet.Range range = deletions[i];
                    notifyItemRangeRemoved(range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    notifyItemRangeInserted(range.startIndex, range.length);
                }

                if (!updateOnModification) {
                    return;
                }

                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    notifyItemRangeChanged(range.startIndex, range.length);
                }
            }
        };
    }

    /**
     * This is equivalent to {@code ListContentRecyclerViewAdapter(data, autoUpdate, true)}.
     *
     * @see #ListContentRecyclerViewAdapter(OrderedRealmCollection, boolean, boolean, RecyclerViewClickListener)
     */
    public ListContentRecyclerViewAdapter(@Nullable OrderedRealmCollection<contentList> data, boolean autoUpdate, RecyclerViewClickListener listener) {
        this(data, autoUpdate, true, listener);
    }

    /**
     * @param data collection data to be used by this adapter.
     * @param autoUpdate when it is {@code false}, the adapter won't be automatically updated when collection data
     *                   changes.
     * @param updateOnModification when it is {@code true}, this adapter will be updated when deletions, insertions or
     *                             modifications happen to the collection data. When it is {@code false}, only
     *                             deletions and insertions will trigger the updates. This param will be ignored if
     *                             {@code autoUpdate} is {@code false}.
     */
    public ListContentRecyclerViewAdapter(@Nullable OrderedRealmCollection<contentList> data, boolean autoUpdate,
                                          boolean updateOnModification, RecyclerViewClickListener listener) {
        if (data != null && !data.isManaged())
            throw new IllegalStateException("Only use this adapter with managed RealmCollection, " +
                    "for un-managed lists you can just use the BaseRecyclerViewAdapter");
        this.adapterData = data;
        this.hasAutoUpdates = autoUpdate;
        this.listener = hasAutoUpdates ? createListener() : null;
        this.updateOnModification = updateOnModification;
        this.recyclerViewClickListenerlistener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (hasAutoUpdates && isDataValid()) {
            //noinspection ConstantConditions
            addListener(adapterData);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (hasAutoUpdates && isDataValid()) {
            //noinspection ConstantConditions
            removeListener(adapterData);
        }
    }

    /**
     * Returns the current ID for an item. Note that item IDs are not stable so you cannot rely on the item ID being the
     * same after notifyDataSetChanged() or {@link #updateData(OrderedRealmCollection)} has been called.
     *
     * @param index position of item in the adapter.
     * @return current item ID.
     */
    @Override
    public long getItemId(final int index) {
        return index;
    }

    @Override
    public int getItemCount() {
        //noinspection ConstantConditions
        return isDataValid() ? adapterData.size() : 0;
    }

    /**
     * Returns the item associated with the specified position.
     * Can return {@code null} if provided Realm instance by {@link OrderedRealmCollection} is closed.
     *
     * @param index index of the item.
     * @return the item at the specified position, {@code null} if adapter data is not valid.
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public contentList getItem(int index) {
        //noinspection ConstantConditions
        return isDataValid() ? adapterData.get(index) : null;
    }

    /**
     * Returns data associated with this adapter.
     *
     * @return adapter data.
     */
    @Nullable
    public OrderedRealmCollection<contentList> getData() {
        return adapterData;
    }

    /**
     * Updates the data associated to the Adapter. Useful when the query has been changed.
     * If the query does not change you might consider using the automaticUpdate feature.
     *
     * @param data the new {@link OrderedRealmCollection} to display.
     */
    @SuppressWarnings("WeakerAccess")
    public void updateData(@Nullable OrderedRealmCollection<contentList> data) {
        if (hasAutoUpdates) {
            if (isDataValid()) {
                //noinspection ConstantConditions
                removeListener(adapterData);
            }
            if (data != null) {
                addListener(data);
            }
        }

        this.adapterData = data;
        notifyDataSetChanged();
    }

    private void addListener(@NonNull OrderedRealmCollection<contentList> data) {
        if (data instanceof RealmResults) {
            RealmResults<contentList> results = (RealmResults<contentList>) data;
            //noinspection unchecked
            results.addChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<contentList> list = (RealmList<contentList>) data;
            //noinspection unchecked
            list.addChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private void removeListener(@NonNull OrderedRealmCollection<contentList> data) {
        if (data instanceof RealmResults) {
            RealmResults<contentList> results = (RealmResults<contentList>) data;
            //noinspection unchecked
            results.removeChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<contentList> list = (RealmList<contentList>) data;
            //noinspection unchecked
            list.removeChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private boolean isDataValid() {
        return adapterData != null && adapterData.isValid();
    }

    public interface  RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    @Override
    public RowViewHolderList onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_card, parent, false);

        return new RowViewHolderList(v, recyclerViewClickListenerlistener);
    }



    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        contentList content = adapterData.get(position);
        Product product = content.getProduct();
        float total = product.getPrice() * content.getQuantity();
        holder.name.setText(product.getName());
        holder.id.setText(Integer.toString(product.getId()));
        holder.price.setText(content.getQuantity()+" de $"+Float.toString(product.getPrice()));
        holder.total.setText("$"+Float.toString(total));
        if(product.getPhotoUrl().equals("")) {
            Picasso.with(holder.itemView.getContext()).load(R.drawable.no_image).into(holder.ivImage);
        }else{
            Picasso.with(holder.itemView.getContext()).load(product.getPhotoUrl()).resize(100,100).into(holder.ivImage);
        }


    }
    public class RowViewHolderList extends ListContentRecyclerViewAdapter.ViewHolder implements View.OnClickListener{

        private RecyclerViewClickListener listener;

        public RowViewHolderList(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            this.listener = listener;

            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
            //Toast.makeText(itemView.getContext(), "Seleccion "+ adapterData.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
        }


        /*@Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, 0, "Delete");
            //contextMenu.add(0, 1, 0, "Edit");
            setPosition(getAdapterPosition());
        }*/
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivImage;
        private TextView price;
        private TextView name;
        private TextView id;
        private TextView total;


        public ViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            price = itemView.findViewById(R.id.tvPrice);
            name = itemView.findViewById(R.id.tvCompra);
            id = itemView.findViewById(R.id.tvIdProduct);
            total = itemView.findViewById(R.id.tvTotal);

        }

    }
    public int getPosition() {
        return selectedPos;
    }

    public void setPosition(int position) {
        this.selectedPos = position;
    }
}