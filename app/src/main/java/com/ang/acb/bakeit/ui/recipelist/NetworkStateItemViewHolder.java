package com.ang.acb.bakeit.ui.recipelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.NetworkStateItemBinding;

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 *
 * See: https://github.com/googlesamples/android-architecture-components/tree/PagingWithNetworkSample
 */
public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    private NetworkStateItemBinding binding;

    public NetworkStateItemViewHolder(@NonNull NetworkStateItemBinding binding,
                                      final RecipeListViewModel viewModel) {
        super(binding.getRoot());
        this.binding = binding;

        // TODO Trigger retry event on click.
        this.binding.retryButton.setOnClickListener(view -> viewModel.retry());
    }

    public static NetworkStateItemViewHolder create(ViewGroup parent, RecipeListViewModel viewModel) {
        // Inflate view and obtain an instance of the binding class.
        NetworkStateItemBinding binding = NetworkStateItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new NetworkStateItemViewHolder(binding, viewModel);
    }

    public void bindTo(Resource networkState) {
        binding.setResource(networkState);

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }
}
