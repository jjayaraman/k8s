module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  version         = "~> 21.0"

  name    = "jay-eks-cluster"
  kubernetes_version = "1.33"
  endpoint_public_access = true

  vpc_id          = module.jay_vpc.vpc_id
  subnet_ids      = module.jay_vpc.private_subnets

  # EKS managed node group
  eks_managed_node_groups = {
    main= {
      instance_type = "t3.small"
      capacity_type = "ON_DEMAND"

      min_size         = 1
      max_size         = 2
      desired_size     = 1
      disk_size        = 5

      key_name = "jay-eks-key"

      tags = {
        Terraform   = "true"
        Environment = "dev"
        Description = "EKS worker nodes"
        Name = "jay-eks-node"
      }
    }
  }

  tags = {
    Terraform   = "true"
    Environment = "dev"
    description = "EKS cluster"
  }
}

# security group rules to restrict access
resource "aws_security_group_rule" "node_ingress" {
  type              = "ingress"
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["10.0.0.0/16"]  # Only allow VPC internal traffic
  security_group_id = module.eks.node_security_group_id
}