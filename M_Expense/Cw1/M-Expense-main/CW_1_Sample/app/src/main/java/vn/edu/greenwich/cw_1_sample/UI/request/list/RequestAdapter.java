package vn.edu.greenwich.cw_1_sample.UI.request.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.models.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> implements Filterable {
    protected ArrayList<Request> _originalList;
    protected ArrayList<Request> _filteredList;
    protected RequestAdapter.ItemFilter _itemFilter = new RequestAdapter.ItemFilter();

    public RequestAdapter(ArrayList<Request> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Request> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = _filteredList.get(position);

        holder.listItemRequestAmount.setText(request.getAmount());
        holder.listItemRequestDate.setText(request.getDate());
        holder.listItemRequestTime.setText(request.getTime());
        holder.listItemRequestType.setText(request.getType());
        holder.listItemRequestContent.setText(request.getContent());
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView listItemRequestDate, listItemRequestTime, listItemRequestType, listItemRequestContent,listItemRequestAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemRequestAmount = itemView.findViewById(R.id.listItemRequestAmount);
            listItemRequestDate = itemView.findViewById(R.id.listItemRequestDate);
            listItemRequestTime = itemView.findViewById(R.id.listItemRequestTime);
            listItemRequestType = itemView.findViewById(R.id.listItemRequestType);
            listItemRequestContent = itemView.findViewById(R.id.listItemRequestContent);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Request> list = _originalList;
            final ArrayList<Request> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Request request : list) {
                String filterableString = request.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(request);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Request>) results.values;
            notifyDataSetChanged();
        }
    }
}